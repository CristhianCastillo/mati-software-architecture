package co.com.product.master.consumer.config;

import co.com.product.master.consumer.adapter.NodePrincipalAdapter;
import co.com.product.master.consumer.adapter.NodeSecondaryAdapter;
import co.com.product.master.model.reserve.gateways.PrincipalReserveGateway;
import co.com.product.master.model.reserve.gateways.SecondaryReserveGateway;
import io.micrometer.core.instrument.Counter;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class RestConsumerConfig {

    @Bean(name = "principalNodeWebClient")
    @Qualifier("principalNodeWebClient")
    public WebClient principalNodeWebClient(NodePrincipalConsumerConfig nodePrincipalConsumerConfig) {
        return WebClient.builder()
                .baseUrl(nodePrincipalConsumerConfig.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(getClientHttpConnector(nodePrincipalConsumerConfig.getTimeoutConnect().intValue(),
                        nodePrincipalConsumerConfig.getTimeoutConnect(), nodePrincipalConsumerConfig.getTimeoutRead()))
                .build();
    }

    @Bean
    public PrincipalReserveGateway principalReserveGateway(@Qualifier("principalNodeWebClient") WebClient principalNodeWebClient,
                                                           NodePrincipalConsumerConfig nodePrincipalConsumerConfig,
                                                           @Qualifier("principalNodeReserveProductsCount") Counter principalNodeReserveProductsCount) {
        return new NodePrincipalAdapter(principalNodeWebClient, nodePrincipalConsumerConfig.getDefaultPath(), principalNodeReserveProductsCount);
    }

    @Bean(name = "secondaryNodeWebClient")
    @Qualifier("secondaryNodeWebClient")
    public WebClient secondaryNodeWebClient(NodeSecondaryConsumerConfig nodeSecondaryConsumerConfig) {
        return WebClient.builder()
                .baseUrl(nodeSecondaryConsumerConfig.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(getClientHttpConnector(nodeSecondaryConsumerConfig.getTimeoutConnect().intValue(),
                        nodeSecondaryConsumerConfig.getTimeoutConnect(), nodeSecondaryConsumerConfig.getTimeoutRead()))
                .build();
    }

    @Bean
    public SecondaryReserveGateway secondaryReserveGateway(@Qualifier("secondaryNodeWebClient") WebClient secondaryNodeWebClient,
                                                           NodeSecondaryConsumerConfig nodeSecondaryConsumerConfig,
                                                           @Qualifier("secondaryNodeReserveProductsCount") Counter secondaryNodeReserveProductsCount) {
        return new NodeSecondaryAdapter(secondaryNodeWebClient, nodeSecondaryConsumerConfig.getDefaultPath(), secondaryNodeReserveProductsCount);
    }


    private ClientHttpConnector getClientHttpConnector(Integer timeout, Long timeoutWrite, Long timeoutRead) {
        /*
        IF YO REQUIRE APPEND SSL CERTIFICATE SELF SIGNED: this should be in the default cacerts trustore
        */
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeoutRead, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeoutWrite, MILLISECONDS));
                }));
    }

}
