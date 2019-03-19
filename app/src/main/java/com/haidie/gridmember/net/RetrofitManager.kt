package com.haidie.gridmember.net
import com.haidie.gridmember.BuildConfig
import com.haidie.gridmember.MyApplication
import com.haidie.gridmember.api.ApiService
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.utils.NetworkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by admin2
 *  on 2018/08/09  11:22
 * description
 */
object RetrofitManager {
    private var client: OkHttpClient? = null
    private var retrofit: Retrofit? = null
    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }
    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                    // Provide your custom parameter here
//                    .addQueryParameter("phoneSystem", "")
//                    .addQueryParameter("phoneModel", "")
                    .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
//                    .addHeader("token", token)
                    .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable(MyApplication.context)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            if (NetworkUtil.isNetworkAvailable(MyApplication.context)) {
                val maxAge = 0
                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                response.newBuilder()
                        .header("Cache-Control", "public,max-age$maxAge")
                        .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build()
            } else {
                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .addHeader("Cache-Control", "public,only-if-cached,max-stale=$maxStale")
                        .removeHeader("nyn")
                        .build()
            }
            response
        }
    }

    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(RetrofitManager::class.java) {
                if (retrofit == null) {
                    //添加一个log拦截器,打印所有的log
                    val httpLoggingInterceptor = HttpLoggingInterceptor()
                    //可以设置请求过滤的水平,body,basic,headers
                    httpLoggingInterceptor.level = if (BuildConfig.LOG_DEBUG)
                    HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    //设置 请求的缓存的大小跟位置
//                    val cacheFile = File(MyApplication.context.cacheDir, "cache")
//                    val cache = Cache(cacheFile, 1024 * 1024 * 50)//50Mb 缓存的大小
                    client = OkHttpClient.Builder()
//                            .addInterceptor(addQueryParameterInterceptor())  //参数添加
//                            .addInterceptor(addHeaderInterceptor()) // token过滤
                            .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
//                            .cache(cache) //添加缓存
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .build()

                    retrofit = Retrofit.Builder()
                            .baseUrl(UrlConstant.BASE_URL_HOST)
                            .client(client!!)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
            }
        }
        return retrofit
    }
}