package com.cmm.worldartapk.fragment;


import java.util.HashMap;

public class FragmentFactory {

	// 测试1
	private static final int TEST_1 = 0;
	// 测试2
	private static final int TEST_2 = 1;
	// 测试3
	private static final int TEST_3 = 2;

	private static HashMap<Integer, WebViewBaseFragment> mFragments = new HashMap<Integer, WebViewBaseFragment>();

	public static WebViewBaseFragment createFragment(int position) {

		// 从缓存中取出
		WebViewBaseFragment fragment = mFragments.get(position);
		if (null == fragment) {
			switch (position) {
			case TEST_1:
				fragment = new Pager1_Fragment();
				break;
			case TEST_2:
				fragment = new Pager2_Fragment();
				break;
			case TEST_3:
				fragment = new Pager3_Fragment();
				break;

			default:
				break;
			}
			// 把frament加入到缓存中
			mFragments.put(position, fragment);
		}
		return fragment;
	}
}
