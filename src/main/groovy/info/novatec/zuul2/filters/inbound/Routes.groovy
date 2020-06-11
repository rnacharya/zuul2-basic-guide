package info.novatec.zuul2.filters.inbound

import com.netflix.zuul.context.SessionContext
import com.netflix.zuul.filters.http.HttpInboundSyncFilter
import com.netflix.zuul.message.http.HttpRequestMessage
import com.netflix.zuul.netty.filter.ZuulEndPointRunner
import info.novatec.zuul2.filters.endpoint.NotFoundEndpoint

/**
 * Routing filter on base of HttpInboundSyncFilter.<br/>
 * URIs of the proxied backend services are defined in {@code application.properties}
 */
class Routes extends HttpInboundSyncFilter {

    @Override
    HttpRequestMessage apply(HttpRequestMessage request) {
        SessionContext context = request.getContext()
        switch (request.getInboundRequest().getQueryParams().get("src").get( 0 )) {
            case "books":
                context.setEndpoint(ZuulEndPointRunner.PROXY_ENDPOINT_FILTER_NAME)
                context.setRouteVIP("some-service-1")
				request.setPath("/books");
                break

            case "snacks":
                context.setEndpoint(ZuulEndPointRunner.PROXY_ENDPOINT_FILTER_NAME)
                context.setRouteVIP("some-service-2")
				request.setPath("/snacks");
                break

            default:
                context.setEndpoint(NotFoundEndpoint.class.getCanonicalName())
        }
        return request
    }

    @Override
    int filterOrder() {
        return 0
    }

    @Override
    boolean shouldFilter(HttpRequestMessage request) {
        return true
    }
}
