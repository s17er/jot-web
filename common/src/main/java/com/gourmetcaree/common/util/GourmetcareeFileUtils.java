package com.gourmetcaree.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gourmetcaree.common.dto.MaterialDto;
import com.gourmetcaree.common.exception.ImageWriteErrorException;
import com.gourmetcaree.db.common.constants.MTypeConstants;

public class GourmetcareeFileUtils {
	/**
	 * Webdata用のファイルを作成します。
	 * @param dirPath
	 * @param dirName
	 * @param fileName
	 * @param image
	 * @throws ImageWriteErrorException
	 */
	public static synchronized void createWebdataFile(String dirPath, String dirName, String fileName, byte[] image) throws ImageWriteErrorException {
		createImageFile(dirPath, dirName, fileName, image);
	}


	/**
	 * ファイルシステムにファイルを出力する。
	 * @param dirPath
	 * @param fileName
	 * @param image
	 * @throws ImageWriteErrorException
	 */
	private static synchronized void createImageFile(String dirPath, String dirName, String fileName, byte[] image) throws ImageWriteErrorException {

		BufferedOutputStream bos = null;

		File dir = new File(dirPath, dirName);
		if (!dir.exists() && !dir.isDirectory()) {
			if (!dir.mkdirs()) {
				throw new ImageWriteErrorException("画像ファイルのmkdirs()に失敗しました。");
			}
		}

		try {
			File file = new File(dirPath + System.getProperty("file.separator") + dirName, fileName);

			//画像は存在していなければ作成
			if (!file.exists()) {
				if (!file.createNewFile()) {
					 throw new ImageWriteErrorException("画像ファイルのcreateNewFile()に失敗しました。");
				}
			}

			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(image);
			bos.flush();
		} catch (FileNotFoundException e) {
			 throw new ImageWriteErrorException("画像ファイルの生成時に例外FileNotFoundExceptionが発生しました。" + e);
		} catch (IOException e) {
			 throw new ImageWriteErrorException("画像ファイルの生成時に例外が発生しました。" + e);
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				 throw new ImageWriteErrorException("画像作成時にBufferedOutputStreamのclose()で例外が発生しました。" + e);
			}
		}
	}


	/**
	 * コンテントタイプ
	 * @author ando
	 *
	 */
	public enum ContentType {

		JPEG("image/JPEG"),
		GIF("image/GIF");

		private ContentType(String value) {
			this.value = value;
		};

		private String value;

		public String getValue() {
			return this.value;
		}

		private static Map<String, ContentType> map;
		static {
			map = new HashMap<String, ContentType>();
			map.put("JPEG", JPEG);
			map.put("image/JPEG", JPEG);
			map.put("GIF", GIF);
			map.put("JPEG", JPEG);
		}

		/**
		 * 文字列をEnumに変換する。
		 * デフォルトはJPEGとする。
		 * @param str
		 * @return
		 */
		public static ContentType toEnum(String str) {
			return map.containsKey(str) ? map.get(str) : JPEG;
		}

	}

	/**
	 * ファイル名を生成します。
	 * @param materialKbn
	 * @param contentType
	 * @return
	 */
	public static String createFileName(String materialKbn, ContentType contentType) {

		String extention = ".jpg";

		switch (contentType) {
		case JPEG:
			extention = ".jpg";
			break;
		case GIF:
			extention = ".gif";
			break;
		default:
			break;
		}

		StringBuilder sb = new StringBuilder("");
		sb.append(MTypeConstants.MaterialKbn.TYPE_CD);
		sb.append(materialKbn);
		sb.append(extention);

		return  sb.toString();
	}


	/**
	 * ファイルシステムから画像データを取得します。
	 * @param dirPath
	 * @param dirName
	 * @param materialKbn
	 * @return
	 * @throws ImageWriteErrorException
	 */
	public static MaterialDto getWebdataImageFile(String dirPath, String dirName, String materialKbn) throws ImageWriteErrorException {

		BufferedInputStream bos = null;

		List<ContentType> list = new ArrayList<ContentType>();
		list.add(ContentType.JPEG);
		list.add(ContentType.GIF);

		MaterialDto retDto = new MaterialDto();
		File file = null;

		for (ContentType contentType : list) {
			file = new File(dirPath + System.getProperty("file.separator") + dirName, createFileName(materialKbn, contentType));
			if (file.exists()) {
				retDto.contentType = contentType.getValue();
				break;
			}
		}

		if (file == null) {
			throw new ImageWriteErrorException("画像ファイルが存在しません。");
		}

		try {
			bos = new BufferedInputStream(new FileInputStream(file));
			byte[] dat = new byte[(int)file.length()];
			int result = bos.read(dat);

			if (result == -1) {
				 throw new ImageWriteErrorException("画像ファイルの読み込みに失敗しました。");
			}

			retDto.materialData = dat;

		} catch (FileNotFoundException e) {
			 throw new ImageWriteErrorException("ファイルが存在しませんでした。" + e);
		} catch (IOException e) {
			 throw new ImageWriteErrorException("画像ファイルの読み込みに失敗しました。"+ e);
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				 throw new ImageWriteErrorException("bos.close()に失敗しました。"+ e);
			}
		}

		return retDto;
	}

	/**
	 * ファイルシステムにファイルを出力する。
	 * @param dirPath
	 * @param fileName
	 * @throws ImageWriteErrorException
	 */
	public static synchronized void deleteWebdataDir(String dirPath, String dirName) {
		File dir = new File(dirPath, dirName);
		deleteFile(dir);
	}

	/**
	 * フォルダとそのフォルダ以下を全て削除します。
	 * @param dirOrFile
	 * @return
	 */
    private static synchronized boolean deleteFile(File dirOrFile) {
        if (dirOrFile.isDirectory()) {//ディレクトリの場合
            String[] children = dirOrFile.list();//ディレクトリにあるすべてのファイルを処理する
            for (int i=0; i<children.length; i++) {
                boolean success = deleteFile(new File(dirOrFile, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // 削除
        return dirOrFile.delete();
    }
}
