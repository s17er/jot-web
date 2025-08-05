package com.gourmetcaree.common.util;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * QRコードを作成するクラス.
 * @author Whizz
 */
public class QRCodeUtil {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(QRCodeUtil.class);

	/**
	 * QRコードを作成する
	 * @param webId WEBデータID
	 * @param url 応募用URL
	 * @throws IOException
	 * @throws WriterException
	 * @throws Exception
	 */
	public static byte[] createQrCode(String url, int width, int height) throws IOException, WriterException {

		/* 生成処理 */
		ConcurrentHashMap<EncodeHintType, Object> concurrentHashMap = new ConcurrentHashMap<>();

		/* エラー訂正レベル指定 */
		concurrentHashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

		/* エンコーディング指定 */
		concurrentHashMap.put(EncodeHintType.CHARACTER_SET, com.gourmetcaree.common.constants.Constants.ENCODING);

		/* マージン指定 */
		concurrentHashMap.put(EncodeHintType.MARGIN, 4);

		/* QRコード */
		QRCodeWriter writer = new QRCodeWriter();
		ByteArrayOutputStream byteArrayOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;

		/* QRのバイトコード */
		byte[] imageBytes;

		try {

			BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height, concurrentHashMap);
			BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

			/* バイトコード変換 */
			byteArrayOutputStream = new ByteArrayOutputStream();
			bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
			image.flush();
			ImageIO.write(image, "png", bufferedOutputStream);

			imageBytes = byteArrayOutputStream.toByteArray();

		} finally {

			try {
				byteArrayOutputStream.close();
			} catch (Exception e) {
				log.error("QRコードの生成に失敗しました。(byteArrayOutputStream)" + e.getMessage());
			}

			try {
				bufferedOutputStream.close();
			} catch (Exception e) {
				log.error("QRコードの生成に失敗しました。(bufferedOutputStream)" + e.getMessage());
			}

		}

		return imageBytes;
	}
}
