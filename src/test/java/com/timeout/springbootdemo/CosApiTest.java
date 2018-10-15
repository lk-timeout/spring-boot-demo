package com.timeout.springbootdemo;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;

public class CosApiTest {

	public static void main(String[] args) {
		// 1 初始化用户身份信息(secretId, secretKey)
		COSCredentials cred = new BasicCOSCredentials("", "");
		// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		// clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
		ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
		// 3 生成cos客户端
		COSClient cosClient = new COSClient(cred, clientConfig);
		// bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
		String bucketName = "dev-1-";

//		delete(bucketName, "test/", cosClient);
//		Upload(bucketName, cosClient);
		try {
			transferManagerUpload(bucketName, cosClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param bucketName 桶名称
	 * @param cosClient cos客户端
	 * @Date 2018年10月15日 下午3:43:53
	 */
	public static void Upload(String bucketName, COSClient cosClient) {
		// 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
		// 大文件上传请参照 API 文档高级 API 上传
		File localFile = new File("C:\\Users\\LK.TimeOut.000\\Desktop\\TIM图片20180929171803.jpg");
		// 指定要上传到 COS 上对象键
		// 对象键（Key）是对象在存储桶中的唯一标识。例如，在对象的访问域名 `bucket1-1250000000.cos.ap-guangzhou.myqcloud.com/doc1/pic1.jpg` 中，对象键为 doc1/pic1.jpg, 详情参考 [对象键](https://cloud.tencent.com/document/product/436/13324)
		String key = "test/71803.jpg";
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
		// 上传
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		cosClient.shutdown();
		System.out.println(putObjectResult.getETag());
	}

	/**
	 * @param bucketName 桶名称
	 * @param cosClient 客户端
	 * @Date 2018年10月15日 下午3:45:16
	 */
	public static void downLoad(String bucketName, COSClient cosClient) {
		// 指定要下载到的本地路径
		File downFile = new File("src/main/resources/mydown.txt");
		// 指定要下载的文件所在的 bucket 和对象键
		String key = "test/71803.jpg";
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
		System.out.println(downObjectMeta.getETag());
		cosClient.shutdown();
	}

	public static void delete(String bucketName, String delete_key, COSClient cosClient) {
		cosClient.deleteObject(bucketName, delete_key);
		cosClient.shutdown();
	}

	public static void transferManagerUpload(String bucketName, COSClient cosclient) throws Exception {
		ExecutorService threadPool = Executors.newFixedThreadPool(32);
		// 传入一个 threadpool, 若不传入线程池, 默认 TransferManager 中会生成一个单线程的线程池。
		TransferManager transferManager = new TransferManager(cosclient, threadPool);
		// .....(提交上传下载请求, 如下文所属)
		// 关闭 TransferManger
		File localFile = new File("C:\\Users\\LK.TimeOut.000\\Desktop\\TIM图片20180929171803.jpg");
		// 指定要下载的文件所在的 bucket 和对象键
		String key = "data/71803.jpg";
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
		// 本地文件上传
		com.qcloud.cos.transfer.Upload upload = transferManager.upload(putObjectRequest);
		// 等待传输结束（如果想同步的等待上传结束，则调用 waitForCompletion）
		UploadResult uploadResult = upload.waitForUploadResult();
		System.out.println(uploadResult.getETag());
		System.out.println(uploadResult.getDateStr());
		transferManager.shutdownNow();
	}

}
