import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StopWatchInterceptor implements IClientInterceptor {
    private static final Logger log = LoggerFactory.getLogger(StopWatchInterceptor.class);

    private StopWatch stopWatch = new StopWatch();

    @Override
    public void interceptRequest(IHttpRequest theRequest) {
        stopWatch.reset();
        stopWatch.start();
        log.info("--> Starting request at: {}", stopWatch.getStartTime());
    }

    @Override
    public void interceptResponse(IHttpResponse theResponse) throws IOException {
        stopWatch.stop();

        log.info("--> Got response. time taken: {} milli-seconds.\nDuration: {}", stopWatch.getTime(), stopWatch);
    }
}
