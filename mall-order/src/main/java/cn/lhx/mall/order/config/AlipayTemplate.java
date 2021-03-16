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
    private String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCXfV43RzEC8K1zLMQKMrTUZ4+7NkxfJE/GIoqI8st+eNFKyL2kg3dDr/aVHK77oiVC3QCCzgMOrFHmQHG66XbuF4mKzQFKt9cT8eEbXvm9ll+Tlhns034JeZ8CQJ0FHDoHpAkyMpSK0CgbyMiCSata98iC+HAY14KzAaSPgtq1AJm+QY6UqSLGEH66MgMPErooJpHMqysr4C3tH3WgW1VsKu+3labKK2vzon8arLRnuy00bLXOqiNfRQzFVH1Zr1zhV5z93lCdSIMLKxB/+EjpQnJ2Uq7Y+tIuVtZEehV99fwuGoWdIkOUNS073Nais3p0Jwv4uX6K+wyAGNTePx61AgMBAAECggEAFWLZfixKuwOHOQ9EzbvC04mbPO65cWUK7gnKTPMZz0z3Hvuf6i21FJUu1nYqLdQCNqJEhDLZM6UYGs1WQPDcnVgtTq8KU6YIcwRepyKNVzp3W7qBBnIFBQlqbUL559LITnPV6IRDfCVOxD1gMSR9UGZSpEq2aItkJClDNg9fGNY5Q8qeNvnpfV0Mx17CKPxDbWa3v7FCsz5HHqdwyCdt8fFzvH8yz+I3tAewx0MsWlAOpi15MqsTUpXwNXTl6IMMttjaf0zxl8yBj50SzdjblQGJEJOxHxYngRCdxX5IEX0+CRcp0JV0+ocwTVTeclCga+WuCh78HSiu8QQiH9nKsQKBgQDFSe/emckkWwe12datEvdc704QEFUDGK37fikftRkBYEkD4kKLBPUb1zT+qZtdigA2JIWBQs2/lkwNl9IEha6e2bM37StVAGMKWTFeU+KvTm43P7mtlss1c4SbOu8LxOXFlwKSY19ji+eyRQWu44fEk0BeV3c6aV3b7D+mqwnKhwKBgQDEklD3xDI3Gw3RDhR1iQtqPHBThBV0NUIRweaGL6cZPO4MpqXN/ROSPBlAuDxLF9NJrWVJIPAJF9oKQywMp+0eRJUx0RMG/gOmLk4wCzjd8+T/RxFgXYsuvJa3tsEBif0UNxoVUlohOP8m8dPXTk3VoPs1/d5fPl5SSZvHDEBv4wKBgQCDMl3/jAlE0fxYpFqskdZJySICcLn36yuX+Rfj7AyK67riiE50IIHXV8/4IlYdaC4n0zWs9f3ZEFvXFkUlmLi9DkyuPlv4lN9H/HRmJCa1KdUFjK0vkP3lt8JRcGmTTAb41Slyq/69NkaIOHpFCS46PSorEsNI5tTx8TOK5GcIdQKBgQC0C3xD5B1GYe4+zBGrMhIVhTQxGqr33IwebaZyw5TexfMX/U+io6nYaFmM8PKwqSrqsfoyiAuzmHgqrCndkbXa4mBw15oA7opzHLiawrycdvcJZpOg2y1Paob10FtpyHTYnjuyydIp2eXCSv+Iln0uLZvKcTPLsvl7j1Yyc6NVlwKBgG2mIOiYTCjF4LmcscTeHFjJ2tt8vvtkCcbyMrZxU2I8dQnTzBf4XzMexFpt/8nnvsPtSBvUiHO5kb0l29J1b3/G1cpZiWwKnX8DXkTjWT1ksyc64nxTrJqHC/OCE+HbyYHFKbOkNzPfvDAuW25U/DmqZ8R1SzIaCV9kFEM/iL9a";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoUp73H7UOBuo88MvcfyL3eW8ljdpspZMREbi+yHslxKesnDaQNwE79AItV8inpIjjKej+i0/8vP+dnYOtIDPEgUF5Yk7EN47VmcHlUczZonZFIF23StfXPCIG3pulNCnCAcble2+TUgYXWTQxSVQqnmTadWtvU3SZLqL3+neTT+22QK8KeOok+X4HuGehyGjttn52ZZybBy56+PB2wgqysbu+OCrPlJaphnvQ4ixnxzCmiELGfwukdYDS76CEOP+9gAX0YLEe8O7GhzxppwqHrrEJCaXItKhFphUVpcGaOuuS870nA2KSAP5hStTjentBJbjM/0NHRfLMa5LLHCqrwIDAQAB";

    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private String notify_url;
    //TODO 订单异步通知，收单未做
    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private String return_url = "http://member.mall.com/memberOrder.html";

    // 签名方式
    private String sign_type = "RSA2";

    // 字符编码格式
    private String charset = "utf-8";

    private String timeout= "30m";

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
                + "\"body\":\""+body  +"\","
                + "\"timeout_express\":\""+ timeout +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        // alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //         + "\"total_amount\":\""+ total_amount +"\","
        //         + "\"subject\":\""+ subject +"\","
        //         + "\"body\":\""+ body +"\","
        //         + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
