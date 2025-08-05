package com.gourmetcaree.shop.pc.util.action.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.dto.ShopListMaterialDto;
import com.gourmetcaree.common.exception.ImageWriteErrorException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.WebdataFileUtils;
import com.gourmetcaree.db.common.entity.TMaterial;
import com.gourmetcaree.db.common.entity.TShopListMaterial;
import com.gourmetcaree.db.common.service.MaterialService;
import com.gourmetcaree.db.common.service.ShopListMaterialService;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.util.form.util.ImageUtilityForm;

/**
 * 画像処理を扱うユーティリティアクションクラス
 * @author Takahiro Ando
 * @version 1.0
 *
 */
public class ImageUtilityAction extends PcShopAction {

	private static Logger log = Logger.getLogger(ImageUtilityAction.class);

	/** 素材サービス */
	@Resource
	protected MaterialService materialService;

	/** 店舗一覧素材サービス */
	@Resource
	private ShopListMaterialService shopListMaterialService;

	/**
	 * 画像ユーティリティフォーム
	 */
	@Resource
	@ActionForm
	protected ImageUtilityForm imageUtilityForm;

	/**
	 * 画像イメージのバイトのデータを出力ストリームにセットする。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "displayImage/{webId}/{materialKbn}")
	public String displayImage() {
		handleImage();
		return null;
	}

//	/**
//	 * 画像イメージのバイトのデータを出力ストリームにセットする。
//	 * 同時に画像ファイルをファイルシステムに出力する。
//	 * mod_rewriteによるキャッシュ機能使用時用
//	 *
//	 * * @return
//	 */
//	@Execute(validator = false, urlPattern = "displayImageCache/{jobOfferId}/{imageKbn}/{imageNo}/{imageHashKey}")
//	public String displayImageCache() {
//
//		handleImage();
//		return null;
//	}

	/**
	 * 求人ID、画像区分、画像No、ハッシュを指定し、検索を行います。
	 * 取得したファイルをストリームに出力し、同時にファイルシステムに書き出す。
	 */
	protected void handleImage() {

		try {
			TMaterial entity = materialService.getMaterialEntity(
					NumberUtils.toInt(imageUtilityForm.webId),
					NumberUtils.toInt(imageUtilityForm.materialKbn));

			log.debug("素材を取得しました。" + entity);

			//ストリームに出力
			outputToStream(entity);

//			//ファイルに出力
//			createImageCache(tShopImage);

			//GCを促すため、明示的にnullを代入
			entity = null;

		} catch (Exception e) {
			 log.warn("素材データの操作時に例外が発生しました。" + e.getMessage());
		} finally {

		}
	}

//	/**
//	 * ファイルシステムにキャッシュ用ファイルを出力する。
//	 * @param shopImage
//	 */
//	private void createImageCache(TShopImage shopImage) {
//
//		//画像ファイルのパスを生成
//		StringBuilder sb = new StringBuilder("");
//		sb.append(getCommonProperty("vigor.cacheImagePath"));
//		sb.append(imageUtilityForm.jobOfferId);
//		sb.append("_");
//		sb.append(imageUtilityForm.imageKbn);
//		sb.append("_");
//		sb.append(imageUtilityForm.imageNo);
//		sb.append("_");
//		sb.append(VigorUtil.createUniqueKey(shopImage.insertDatetime));
//		sb.append(".jpg");
//
//		BufferedOutputStream bos = null;
//
//		try {
//			File file = new File(sb.toString());
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				log.warn("キャッシュ用画像ファイルのcreateNewFile()で例外が発生しました。" + e.getMessage());
//				throw e;
//			}
//
//			bos = new BufferedOutputStream(new FileOutputStream(file));
//			bos.write(shopImage.image);
//			bos.flush();
//
//		} catch (FileNotFoundException e) {
//			 log.warn("キャッシュ用画像ファイルの生成時に例外FileNotFoundExceptionが発生しました。" + e.getMessage());
//		} catch (IOException e) {
//			 log.warn("キャッシュ用画像ファイルの生成時に例外が発生しました。" + e.getMessage());
//		} finally {
//			try {
//				bos.close();
//			} catch (IOException e) {
//				log.warn("BufferedOutputStreamのclose()でに例外が発生しました。" + e.getMessage());
//			}
//		}
//	}

	/**
	 * responseのアウトプットストリームに書き出します。
	 * @param res
	 * @param binArray
	 */
	private void outputToStream (TMaterial entity) {

		BufferedOutputStream bos = null;

		try {
			//コンテンツのタイプを指定
			response.setContentType(entity.contentType);

			//MIMEの基準では76文字ごとに改行コードが入るため改行コードを除去
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(Base64Util.decode(entity.materialData.replaceAll("\\n", "")));

			bos.flush();
			log.debug("素材を出力します。");
		} catch (FileNotFoundException e) {
			log.warn(e.getMessage());
		} catch (IOException e) {
			log.warn(e.getMessage());
		} finally {
			try {
				bos.close();
			} catch (Exception e) {
				log.warn("素材の出力に失敗しました。" + e.getMessage());
			}

			//GCを促すため明示的にnullをセット
			entity = null;
		}
	}

	/**
	 * 店舗一覧のキャッシュを作成して表示する。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "displayShopListImageCache/{shopListId}/{materialKbn}/{uniqueKey}")
	public String displayShopListImageCache() {
		handleShopListImageCache();
		return null;
	}

	/**
	 * 店舗一覧の画像キャッシュを作成
	 */
	private void handleShopListImageCache() {
		try {
			TShopListMaterial entity = shopListMaterialService.getMaterialEntity(
					NumberUtils.toInt(imageUtilityForm.shopListId),
					NumberUtils.toInt(imageUtilityForm.materialKbn));

			log.debug("素材を取得しました。" + entity);

			//ストリームに出力
			outputToStream(entity);

			//ファイルに出力
			createImageCache(entity);

			//GCを促すため、明示的にnullを代入
			entity = null;
		} catch (Exception e) {
			 log.warn("素材データの操作時に例外が発生しました。" + e.getMessage());
		} finally {

		}
	}


	/**
	 * responseのアウトプットストリームに書き出します。
	 * @param res
	 * @param binArray
	 */
	private void outputToStream (TShopListMaterial entity) {

		BufferedOutputStream bos = null;

		try {
			//コンテンツのタイプを指定
			response.setContentType(entity.contentType);

			//MIMEの基準では76文字ごとに改行コードが入るため改行コードを除去
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(Base64Util.decode(entity.materialData.replaceAll("\\n", "")));

			bos.flush();
			log.debug("素材を出力します。");
		} catch (FileNotFoundException e) {
			log.warn(e.getMessage());
		} catch (IOException e) {
			log.warn(e.getMessage());
		} finally {
			try {
				bos.close();
			} catch (Exception e) {
				log.warn("素材の出力に失敗しました。" + e.getMessage());
			}

			//GCを促すため明示的にnullをセット
			entity = null;
		}
	}

	/**
	 * responseのアウトプットストリームに書き出します。
	 * @param res
	 * @param binArray
	 */
	private void outputToStream (ShopListMaterialDto dto) {

		BufferedOutputStream bos = null;

		try {
			//コンテンツのタイプを指定
			response.setContentType(dto.contentType);

			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(dto.materialData);

			bos.flush();
			log.debug("素材を出力します。");
		} catch (FileNotFoundException e) {
			log.warn(e.getMessage());
		} catch (IOException e) {
			log.warn(e.getMessage());
		} finally {
			try {
				bos.close();
			} catch (Exception e) {
				log.warn("素材の出力に失敗しました。" + e.getMessage());
			}

			//GCを促すため明示的にnullをセット
			dto = null;
		}
	}

	/**
	 * 店舗一覧の画像を出力します。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "displayShopListImage/{materialKbn}/{shopListId}")
	public String displayShopListImage() {
		if (StringUtil.isEmpty(imageUtilityForm.materialKbn)) {
			log.warn("素材区分が指定されていません。");
			return null;
		}
		handleShopListImage();
		return null;
	}

	/**
	 * session用フォルダから
	 * 取得したファイルをストリームに出力
	 */
	private void handleShopListImage() {
		String dirPath = getCommonProperty("gc.shopList.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder();
		dirName.append(imageUtilityForm.shopListId);
		dirName.append("_");
		dirName.append(session.getId());

		ShopListMaterialDto dto = null;
		try {
			dto = WebdataFileUtils.getShopListImageFile(dirPath, dirName.toString(), imageUtilityForm.materialKbn);
			outputToStream(dto);
		} catch (ImageWriteErrorException e) {
			log.warn("素材データの操作時に例外が発生しました。" + e);
		} finally {
			dto = null;
		}

	}

	/**
	 * ファイルシステムにキャッシュ用ファイルを出力する。
	 * @param entity
	 */
	private void createImageCache(TShopListMaterial entity) {

		//画像ファイルのパスを生成
		StringBuilder sb = new StringBuilder("");
		sb.append(getCommonProperty("gc.front.cache.image.shopList.dir.path"));
		sb.append(imageUtilityForm.shopListId);
		sb.append("_");
		sb.append(imageUtilityForm.materialKbn);
		sb.append("_");
		sb.append(GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
		sb.append(".jpg");

		BufferedOutputStream bos = null;

		try {
			File file = new File(sb.toString());
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.warn("キャッシュ用画像ファイルのcreateNewFile()で例外が発生しました。" + e.getMessage());
				throw e;
			}

			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(Base64Util.decode(entity.materialData.replaceAll("\\n", "")));
			bos.flush();

		} catch (FileNotFoundException e) {
			 log.warn("キャッシュ用画像ファイルの生成時に例外FileNotFoundExceptionが発生しました。" + e.getMessage());
		} catch (IOException e) {
			 log.warn("キャッシュ用画像ファイルの生成時に例外が発生しました。" + e.getMessage());
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				log.warn("BufferedOutputStreamのclose()でに例外が発生しました。" + e.getMessage());
			}
		}
	}

}
