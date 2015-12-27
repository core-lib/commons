package com.qfox.network;

import org.junit.Test;

import com.qfox.network.downloader.AsynchronousDownloader;
import com.qfox.network.downloader.CallbackAdapter;
import com.qfox.network.downloader.Downloader;
import com.qfox.network.downloader.ListenerAdapter;
import com.qfox.network.downloader.URLDownloader;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author yangchangpei 646742615@qq.com
 *
 * @date 2015年8月14日 下午4:04:04
 *
 * @version 1.0.0
 */
public class URLDownloaderTests {

	@Test
	public void testConcurrent() throws Exception {
		URLDownloader.download("http://qfox.oss-cn-shenzhen.aliyuncs.com/upload/video/CUSHOW/fd84dffb-f004-4f4c-9b15-780d1b8e27af.mp4")
		.concurrent(3)
		.times(3)
		.listener(new ListenerAdapter() {

			@Override
			public void start(Downloader<?> downloader, long total) {
				System.out.println(System.currentTimeMillis());
			}

			@Override
			public void progress(Downloader<?> downloader, long total, long downloaded) {
				System.out.println(downloaded + "/" + total);
			}

			@Override
			public void finish(Downloader<?> downloader, long total) {
				System.out.println(System.currentTimeMillis());
			}

		})
		.callback(new CallbackAdapter() {
			@Override
			public void complete(AsynchronousDownloader<?> downloader, boolean success, Exception exception) {
				if (exception != null) {
					exception.printStackTrace();
				}
			}
		})
		.to("/Users/yangchangpei/Documents/download.mp4");
		Thread.sleep(1000 * 1000);
	}

}
