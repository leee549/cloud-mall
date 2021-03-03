package cn.lhx.mall.order.config;

import cn.lhx.mall.order.vo.PayVo;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private String app_id = "2021000117614887";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCfEwuglGAMAoBwy+TFG5GjG3jeIsuDebGSxBUtrYWrKN0NjVheai8V14CdDjKWDoajYHZqL8UAx39aowqwwUyky54Sbx7EpSex2l5A6U62JKd99qH3n1L26KlWmBTD9vG4DYvwqjdQhC5Va5z0tg9Cv2B0RxKadcQIm+kEcR+guszZEWyqJZKeEyuqPUA7HLmfujSKWlmmUNQXybsk8xN9g6taR4pcR4odNRDQZH1MfCuDUup9DfwHpNcJHq/lNIlt3Zy6qhM8pPAsl0aLeZkRvOQoTQQyFFoVW9EsZjwUhAeLD3mkVisY/cUjCKXOJ7xvF9tfy4nQOeEEU72ImJIDAgMBAAECggEBAJP2ZCdOO/SP0yaYEMfit9mOcSO2G7TEWg6ZAtYFBENLdCnp4luXywo29HOhxB3djxCiZiKpIkZyShxlcUkt03upxfOBlo30zPJamrcoDEXR3FWHNeqdMM9nZDOerBoHNZksSJFn0qG7AVoFCe549cLxPeeKQFyZ0+jqFJlEG9Ykz5chDskoXkIr8Xl6n/brY+6JgwqhoOIhg+gJm3EEUcKajETerhcwluL8419bsymMCY9vFj0FxpaiieTIcuRg1qUDvPP9WpJ1tD6EDZ2AZA/am36HIYwG9FoXgc8KdGFPsWaTCwRk5mvcZ1Q9pzWYUz4xf30bKnHi7Dkzf3on53kCgYEA6SPosZwfTtHCh+A9qA5O36kRDlPmFF2TSh4aKVKsepxruuXvImfpS5G8hxEUkTzmDvXef1ZhGHqvAqsmzOMUhy1zOF0GErnh1zTf0i3Jd42mL5mRFPF93dG//QjYPD8Tjya1M9vpTT55ameEHq7L10BHwd9TfzRhsRNqz9hXJb0CgYEArqv/Rv/d/mCbw0I35Vbfp2a8fn/GgQLVRk5Ga183uSyRARdgRI2OZnE3XBFd5WDdDuTHAQP6iS7gmlP40gasnRbkoeyA7eL34gHdnG52AcILw5djAkix7pnGideq9jr5wSxVQpKXdp8T0HJ7xio977aQcbtRjfQXl24NokMOsr8CgYEAqN9g2t36c45O+Tr9jjCnAy0kdJtjChdp7AL38jy3QKzy/9Mzr1lTTHDI/ZDqs/VHs0h3HQeFl0SuZiFxHnHR2cSbNBV1o7TPGKJtCYIu75FD8jCrPV9bS4R2K6PvdVw/H67rDCBb0p7RNbi7o7n7XvRpxc2tXnOj267z7I+JOIUCgYBxaUu+lbNVzvRTu1L/nYBp+NK6b8w7R9OiaoBehtxvZvhfdQYUfLTQ3wHKzZvhqxrc6L4tcp/hIobCrXMJKzw4YvzgBedQjeXU4NE7GJ2WVp+8xzaJ7Rlaio3WQinG5lPzkQ1qxDOD2ZXL6gljiVndovP2ZGF3gd0vfU1s5Y1NkQKBgBtpfR1Jxb/7MWkG3Iy5KuPMDM26DxM5eaUg+jYf7aUn8BNFw9j9YNF0eagc+yPtJ95mB/rO0mbsNet9FQ09oB2ECQpTvmSsAcNCdANwboVjPFmyH83AHT9UJUUgn4HJuQbFA9ehAXiErgS5CKpyTLsec43DvjiWAzq/alvtiFWh";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoUp73H7UOBuo88MvcfyL3eW8ljdpspZMREbi+yHslxKesnDaQNwE79AItV8inpIjjKej+i0/8vP+dnYOtIDPEgUF5Yk7EN47VmcHlUczZonZFIF23StfXPCIG3pulNCnCAcble2+TUgYXWTQxSVQqnmTadWtvU3SZLqL3+neTT+22QK8KeOok+X4HuGehyGjttn52ZZybBy56+PB2wgqysbu+OCrPlJaphnvQ4ixnxzCmiELGfwukdYDS76CEOP+9gAX0YLEe8O7GhzxppwqHrrEJCaXItKhFphUVpcGaOuuS870nA2KSAP5hStTjentBJbjM/0NHRfLMa5LLHCqrwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private String return_url = "http://member.mall.com/memberOrder.html";

    // 签名方式
    private String sign_type = "RSA2";

    // 字符编码格式
    private String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
