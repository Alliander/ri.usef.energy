package energy.usef.core.service.business;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import energy.usef.core.config.Config;
import energy.usef.core.config.ConfigParam;
import energy.usef.core.data.participant.Participant;
import energy.usef.core.data.participant.ParticipantRole;
import energy.usef.core.data.xml.bean.message.USEFRole;
import energy.usef.core.exception.BusinessException;
import energy.usef.core.service.business.error.ParticipantDiscoveryError;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.powermock.reflect.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)public class ParticipantDiscoveryServiceCompatibilityTest {
    private ParticipantListBuilder participantListBuilder;
    private ParticipantDiscoveryService service;

    private final static String BODY = "{\"domain\":\"aggregator.energy\",\"role\":\"AGR\",\"version\":\"2015\",\"publicKeys\":\"cs1.dnti08IOXIZvTYH54U228fG7hQCEtoSrcp7CHL1yqoc=\",\"url\":\"https://aggregator.energy/USEF/2015/SignedMessage\"}";

    @Rule
    public WireMockRule usefEndpoint = new WireMockRule(6667);

    @Mock
    private Config config;

    @Before
    public void setUp() {
        service = new ParticipantDiscoveryService();
        participantListBuilder = new ParticipantListBuilder();
        Whitebox.setInternalState(service, "config", config);
        setInternalState(service, "participantListBuilder", participantListBuilder);

        usefEndpoint.stubFor(WireMock.post(urlEqualTo("/api/participants/aggregator.energy/AGR")).willReturn(aResponse().withBody(BODY).withHeader("content-type","application/json;charset=UTF-8").withStatus(200)));
    }

    @Test
    public void testFindParticipant() {
        Mockito.when(config.getBooleanProperty(Matchers.eq(ConfigParam.BYPASS_DNS_VERIFICATION))).thenReturn(Boolean.TRUE);
        Mockito.when(config.getProperty(Matchers.eq(ConfigParam.PARTICIPANT_DNS_INFO_FILENAME))).thenReturn(
                "participants_dns_info.yaml");
        try {
            Participant p1 = service.findParticipantInLocalConfiguration("aggregator.energy", USEFRole.AGR);

            Mockito.when(config.getProperty(Matchers.eq(ConfigParam.PARTICIPANT_SERVICE_URL))).thenReturn("http://localhost:6667/api/participants");

            Participant p2 = service.findParticipantInParticipantService("aggregator.energy", USEFRole.AGR);

            assertEquals(p1.getDomainName(), p2.getDomainName());
            assertEquals(p1.getSpecVersion(), p2.getSpecVersion());
            assertEquals(p1.getUsefRole(), p2.getUsefRole());
            assertEquals(p1.getUrl(), p2.getUrl());

            assertNull(p1.getPublicKeys());
            assertNull(p2.getPublicKeys());

            assertEquals(1, p1.getRoles().size());
            assertEquals(1, p2.getRoles().size());

            ParticipantRole r1 = p1.getRoles().get(0);
            ParticipantRole r2 = p2.getRoles().get(0);

            assertEquals(r1.getUsefRole(), r2.getUsefRole());
            assertEquals(r1.getUrl(), r2.getUrl());

            assertNotNull(r1.getPublicKeys());
            assertNotNull(r2.getPublicKeys());

            assertEquals(2, r1.getPublicKeys().size());
            assertEquals(2, r2.getPublicKeys().size());

            assertEquals(r1.getPublicKeys().get(0), r2.getPublicKeys().get(0));
            assertEquals(r1.getPublicKeys().get(1), r2.getPublicKeys().get(1));

        } catch (BusinessException e) {
            assertEquals(ParticipantDiscoveryError.PARTICIPANT_NOT_FOUND, e.getBusinessError());
        }
    }

    @Test
    public void testFindParticipantInLocalConfiguration() {
        Mockito.when(config.getProperty(Matchers.eq(ConfigParam.PARTICIPANT_SERVICE_URL))).thenReturn("");
        Mockito.when(config.getBooleanProperty(Matchers.eq(ConfigParam.BYPASS_DNS_VERIFICATION))).thenReturn(Boolean.TRUE);
        Mockito.when(config.getProperty(Matchers.eq(ConfigParam.PARTICIPANT_DNS_INFO_FILENAME))).thenReturn(
                "participants_dns_info.yaml");
        try {
            String p1 = service.findLocalParticipantUnsigningPublicKey("aggregator.energy", USEFRole.AGR);

            Mockito.when(config.getProperty(Matchers.eq(ConfigParam.PARTICIPANT_SERVICE_URL))).thenReturn("http://localhost:6667/api/participants");
            String p2 = service.findUnsealingKeyInParticipantService("aggregator.energy", USEFRole.AGR);
            assertEquals(p1, p2);
        } catch (BusinessException e) {
            assertEquals(ParticipantDiscoveryError.PARTICIPANT_NOT_FOUND, e.getBusinessError());
        }
    }

    @Test
    public void testFindParticipantInLocalConfiguration2() {
        Mockito.when(config.getProperty(Matchers.eq(ConfigParam.PARTICIPANT_SERVICE_URL))).thenReturn(null);
        Mockito.when(config.getBooleanProperty(Matchers.eq(ConfigParam.BYPASS_DNS_VERIFICATION))).thenReturn(Boolean.TRUE);
        Mockito.when(config.getProperty(Matchers.eq(ConfigParam.PARTICIPANT_DNS_INFO_FILENAME))).thenReturn(
                "participants_dns_info.yaml");
        try {
            String p1 = service.findLocalParticipantUnsigningPublicKey("aggregator.energy", USEFRole.AGR);

            Mockito.when(config.getProperty(Matchers.eq(ConfigParam.PARTICIPANT_SERVICE_URL))).thenReturn("http://localhost:6667/api/participants");
            String p2 = service.findUnsealingKeyInParticipantService("aggregator.energy", USEFRole.AGR);
            assertEquals(p1, p2);
        } catch (BusinessException e) {
            assertEquals(ParticipantDiscoveryError.PARTICIPANT_NOT_FOUND, e.getBusinessError());
        }
    }
}
