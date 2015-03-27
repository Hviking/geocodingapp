package com.beyondb.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipCompressor {
	// 使用java.util包下的工具类对文件进行zip格式压缩，流读取的采用utf-8的解码方式。
	// 参数为原始文件
	// 返回压缩后的字节数组。
	public static byte[] compress(File source, int level) throws IOException {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		ZipOutputStream zos = null;
		try {
			baos = new ByteArrayOutputStream();
//                        zos = new ZipOutputStream(baos, Charset.forName("UTF-8"));
			zos = new ZipOutputStream(baos);
			zos.setLevel(level);
			bis = new BufferedInputStream(new FileInputStream(source));
			zos.putNextEntry(new ZipEntry(source.getName()));
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = bis.read(buf)) != -1) {
				zos.write(buf, 0, length);
			}
			zos.finish();
			byte[] result = baos.toByteArray();
			return result;
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	// 使用java.util包下的工具类对文件进行zip格式压缩，对字节数组进行压缩。
	// 参数为字节数组
	// 返回压缩后的字节数组。
	public static byte[] compress(byte[] source, int level) throws IOException {
		ByteArrayOutputStream baos = null;
		ZipOutputStream zos = null;
		try {
			baos = new ByteArrayOutputStream();
//			zos = new ZipOutputStream(baos, Charset.forName("UTF-8"));
			zos = new ZipOutputStream(baos);
			zos.setLevel(level);
			zos.putNextEntry(new ZipEntry(source.toString()));
			zos.write(source, 0, source.length);
			zos.finish();
			byte[] result = baos.toByteArray();
			return result;
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	// 使用java.util包下的工具类对压缩过的字节数组进行解压缩操作
	// 参数为字节数组
	// 返回解压缩后的字节数组。
	public static byte[] uncompress(byte[] source) throws IOException {
		ZipInputStream zin = null;
		ByteArrayOutputStream baos = null;
		BufferedInputStream bin = null;
		try {
			zin = new ZipInputStream(new ByteArrayInputStream(source));
			bin = new BufferedInputStream(zin);
			baos = new ByteArrayOutputStream();
			ZipEntry entry = null;
			while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {
				byte[] buf = new byte[1024];
				int length = 0;
				while ((length = bin.read(buf)) != -1) {
					baos.write(buf, 0, length);
				}
			}
			byte[] result = baos.toByteArray();
			return result;
		} finally {
			try {
				if (bin != null) {
					bin.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			try {
				if (zin != null) {
					zin.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	// 使用方法
	public static void main(String[] args) {
		try {
			// 压缩方法1：参数1：原始文件；参数2：压缩等级，一般设置为1；返回压缩后的文件字节数组；
			byte[] bytes1 = ZipCompressor.compress(new File(
					"/home/hadoop/a.txt"), 1);
			// 压缩方法2：参数1：原始字节数组；参数2：压缩等级，一般设置为1；返回压缩后的文件字节数组；
			byte[] bytes2 = ZipCompressor.compress(new byte[100], 1);
			//解压缩方法：参数1：从数据库中查询的结果字节数（压缩过的）；返回解压后的字节数组；
			byte[] bytes3 = ZipCompressor.uncompress(new byte[100]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
