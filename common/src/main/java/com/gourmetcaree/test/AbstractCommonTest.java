package com.gourmetcaree.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.PostBindFields;
import org.seasar.framework.util.ResourceUtil;

import com.gourmetcaree.common.dto.LabelValueDto;

/**
 * 基底テストクラス
 * @author Takehiro Nakamori
 *
 */
@RunWith(Seasar2.class)
public abstract class AbstractCommonTest {

	protected final Logger testLog = Logger.getLogger(this.getClass());

	/** コンテナ */
	private S2Container container;

	/** JDBCマネージャ */
	protected JdbcManager jdbcManager;

	protected static final String UT_URL = "http://www.gourmetcaree-tokyo.local";

	protected static final String UT_SSL_URL = "https://www.gourmetcaree-tokyo.local";

	protected static final String UT_CXT_PC = "s-pc-staging";

	protected static final String UT_CXT_MB = "s-mb-staging";


	/**
	 * パッケージからクラスリストを取得
	 * @param pack パッケージ
	 * @return
	 */
	protected List<Class<?>> getClassListFromPackage(Package pack) {
		return getClassListFromPackage(pack.getName());
	}

	/**
	 * パッケージからクラスリストを取得
	 * @param packageName パッケージ名
	 * @return
	 */
	protected List<Class<?>> getClassListFromPackage(String packageName) {
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();

			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				String fileName = resource.getFile();
				String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
				dirs.add(new File(fileNameDecoded));
			}

			List<Class<?>> classList = new ArrayList<Class<?>>(dirs.size());
			for (File directory : dirs) {
				classList.addAll(findClasses(directory, packageName));
			}


//			for (Iterator<Class<?>> it = classList.listIterator(); it.hasNext();) {
//				Class<?> clazz = it.next();
//			}

			return classList;
		} catch (Exception e) {
			throw new RuntimeException(String.format("パッケージ[%s]からクラスを取得中にエラーが発生しました。", packageName), e);
		}
	}


	/**
	 * クラスを取得
	 * @param directory
	 * @param packageName
	 * @return
	 * @throws ClassNotFoundException
	 * @author nakamori
	 */
	private static List<Class<?>> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			String fileName = file.getName();
			if (file.isDirectory()) {
				assert !fileName.contains(".");
				classes.addAll(findClasses(file, packageName + "." + fileName));
			} else if (fileName.endsWith(".class") && !fileName.contains("$")) {
				Class<?> _class;
				try {
					String className = packageName + '.'
							+ fileName.substring(0, fileName.length() - 6);

					_class = Class.forName(className);
				} catch (ExceptionInInitializerError e) {
					// happen, for example, in classes, which depend on
					// Spring to inject some beans, and which fail,
					// if dependency is not fulfilled
					_class = Class.forName(
							packageName
									+ '.'
									+ fileName.substring(0,
											fileName.length() - 6), false,
							Thread.currentThread().getContextClassLoader());
				}
				classes.add(_class);
			}
		}
		return classes;
	}


	/**
	 * コンポーネントの取得
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getComponent(Class<T> clazz) {
		if (container == null) {
			container = SingletonS2ContainerFactory.getContainer();
		}
		return (T) container.getComponent(clazz);
	}

	/**
	 * コンポーネントの取得
	 * @param componentKey
	 * @return
	 */
	protected Object getComponent(String componentKey) {
		if (container == null) {
			container = SingletonS2ContainerFactory.getContainer();
		}

		return container.getComponent(componentKey);
	}


	protected void setDeclaredField(Object obj, Class<?> clazz, String name, Object value) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			throw new RuntimeException("setDeclaredFieldに失敗しました。", e);
		}
	}




	@PostBindFields
	public void postBindFields() {
		ServletContext application = (ServletContext) getComponent("application");
		application.setAttribute("common", getCommonPropertiesMap());
	}



	/**
	 * コモンプロパティのマップを取得します。
	 * @return
	 */
	private Map<Object, Object> getCommonPropertiesMap() {
		try {
			Properties properties = ResourceUtil.getProperties("gourmetcaree.properties");
			Set<Entry<Object, Object>> entrySet = properties.entrySet();
			Map<Object, Object> map = new HashMap<Object, Object>(entrySet.size());
			for (Entry<Object, Object> entry : entrySet) {
				map.put(entry.getKey(), entry.getValue());
			}
			return map;
		} catch (ResourceNotFoundRuntimeException e) {
			Logger.getLogger(this.getClass())
					.warn("getCommonPropertiesMap で propertiesファイルが取得できませんでした。", e);
			return new HashMap<Object, Object>();
		}
	}




	/**
	 * SpecifiedListのアサート
	 * @param expectedList
	 * @param resultList
	 */
	protected void assertLabelValueDtoList(List<LabelValueDto> expectedList, List<LabelValueDto> resultList) {
		if (CollectionUtils.isEmpty(expectedList)) {
			assertTrue(CollectionUtils.isEmpty(resultList));
			return;
		}

		assertEquals(expectedList.size(), resultList.size());

		int index = 0;
		for (LabelValueDto expected : expectedList) {

			LabelValueDto result = resultList.get(index++);

			assertEquals(expected.getLabel(), result.getLabel());
			if (expected.getValue() == null) {
				assertNull(result.getValue());
			} else {
				assertEquals(expected.getValue(), result.getValue());
			}
		}
	}

	protected static void setField(Object obj, Class<?> clazz, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, value);
	}












	/* ************ JUNIT系メソッド ******************/
	@Before
	public void before() {
		testLog.debug("before");
	}

	@After
	public void after() {
		testLog.debug("after");
	}

}
