/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.swingworker;

import com.beyondb.SpatialReference.SpatialReferenceUtil;
import com.beyondb.geocoding.Coordinate;
import com.beyondb.io.MyTableModel;
import com.beyondb.ui.JPanel_txtIntoDB;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author
 */
public class TransformCoordinateSwingWorker extends SuperSwingWorker {

    private String m_SourceCoordinateSys;
    private String m_TargetCoordinateSys;
    private Object[][] m_ColumnData;
    JPanel_txtIntoDB m_Panel;

    public TransformCoordinateSwingWorker(JPanel_txtIntoDB panel) {
        super(panel, panel);
        m_Panel = panel;
    }

    public void setParameters(Object[][] record,
            String sourceCoordinateSys,
            String targetCoordinateSys) {
        m_ColumnData = record;
        m_SourceCoordinateSys = sourceCoordinateSys;
        m_TargetCoordinateSys = targetCoordinateSys;
    }

    @Override
    protected String doInBackground() throws Exception {

        if (!SpatialReferenceUtil.hasCoordinateSys(m_SourceCoordinateSys)) {
            m_Panel.setShowMessage(m_TaskName + "\n没有针对源坐标系的转换工具！\n",
                    JOptionPane.ERROR_MESSAGE);

            return "";
        }
        if (!SpatialReferenceUtil.hasCoordinateSys(m_TargetCoordinateSys)) {

            m_Panel.setShowMessage(m_TaskName + "\n没有针对目标坐标系的转换工具！\n",
                    JOptionPane.ERROR_MESSAGE);
            return "";
        }

        m_Table.setEnabled(false);

        //经纬度坐标
        for (int rowIndex = 0; rowIndex < m_ColumnData.length; rowIndex++) {

            double longitude = Double.parseDouble(String.valueOf(m_ColumnData[rowIndex][0]));
            double latitude = Double.parseDouble(String.valueOf(m_ColumnData[rowIndex][1]));
            Coordinate coord = new Coordinate();
            coord.Latitude = latitude;
            coord.Longitude = longitude;
            if (!(coord.Latitude == 0 && coord.Longitude == 0)) {
                Coordinate retCoord = SpatialReferenceUtil.transform(m_SourceCoordinateSys, m_TargetCoordinateSys, coord);
                if (retCoord != null) {
                    m_ColumnData[rowIndex][0] = retCoord.Longitude;
                    m_ColumnData[rowIndex][1] = retCoord.Latitude;
                } else {
                    setDisplayStop();
                    m_Panel.setShowMessage(m_TaskName + "\n坐标转换失败！\n",
                            JOptionPane.ERROR_MESSAGE);

                    return "";
                }
            }
            setDisplayStatus("正在将地理坐标从【" + m_SourceCoordinateSys + "】转换到【" + m_TargetCoordinateSys
                    + "】\n --- " + (rowIndex + 1) + "/" + m_TableModel.getRowCount());
            setDisplayProgress(100 * (rowIndex + 1) / m_TableModel.getRowCount());

            if (isStop) {
                break;
            }

        }

        if (isStop) {
            return "";
        }
        if (m_ColumnData.length > 0) {
            setDisplayStatus("坐标转换结果正在保存");

            boolean flag = false;
            try {
                flag = ((MyTableModel) m_TableModel).updateColumn(m_Table.getSelectedColumns(), m_ColumnData);
            } catch (Exception e) {
                m_Panel.setShowMessage(m_TaskName + "\n坐标转换结果保存到原文件失败！\n\n" + e.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
                return "";
            }

            if (flag) {
                setDisplayStatus("坐标转换结果保存到原文件成功");
            } else {
                m_Panel.setShowMessage(m_TaskName + "\n坐标转换结果保存到原文件失败！", JOptionPane.ERROR_MESSAGE);
            }
        }

        return "";
    }

    @Override
    protected void done() {
        //done()本身是在EDT中执行的    
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    setDisplayStop();
                    m_Table.setEnabled(true);
                    m_Table.updateUI();

                    String msg = (String) get();
                    if (!msg.isEmpty()) {
                        m_Panel.setShowMessage(msg, JOptionPane.ERROR);
                    }
                } catch (ExecutionException | InterruptedException ex) {
                    Logger.getLogger(TransformCoordinateSwingWorker.class.getName()).log(Level.SEVERE, "", ex);
                } finally {
                    ArrayList<String> btnArrayList = new ArrayList<>();
                    btnArrayList.add("地址解析");
                    btnArrayList.add("常规入库");
                    btnArrayList.add("空间化入库");
                    btnArrayList.add("修改坐标系");
                    m_Panel.setButtonEnable(btnArrayList, true);
                }
            }
        });
    }

}
