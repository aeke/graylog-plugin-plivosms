package org.graylog2.alarmcallbacks.plivo;

import com.google.common.collect.ImmutableMap;
import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.account.Account;
import com.plivo.helper.api.response.message.MessageResponse;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.configuration.Configuration;

import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.streams.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlivoSmsAlarmCallbackTest {

    private static String TEST_AUTH_ID = "***";
    private static String TEST_AUTH_TOKEN = "***";
    private static String TEST_SRC_NUMBER = "***";
    private static String TEST_DST_NUMBER = "***";

    private static final Configuration VALID_CONFIGURATION = new Configuration(ImmutableMap.<String, Object>of(
            "auth_id", TEST_AUTH_ID,
            "auth_token", TEST_AUTH_TOKEN,
            "src_number", TEST_SRC_NUMBER,
            "dst_number", TEST_DST_NUMBER
    ));

    @Mock
    private AlertCondition.CheckResult mockCheckResult;
    private PlivoSmsAlarmCallback plivoSmsAlarmCallback;
    @Mock
    private Account testAccount;
    @Mock
    private Stream testStream;
    @Mock
    private RestAPI plivoApi;
    @Mock
    private MessageResponse messageResponse;


    @Before
    public void setUp() {
        when(mockCheckResult.getResultDescription()).thenReturn("Test");

        plivoApi = new RestAPI(TEST_AUTH_ID, TEST_AUTH_TOKEN, "v1");
        plivoSmsAlarmCallback = new PlivoSmsAlarmCallback();
    }

    @Test
    public void testInitialize() throws AlarmCallbackConfigurationException {
        plivoSmsAlarmCallback.initialize(VALID_CONFIGURATION);
    }

    @Test
    public void checkConfigurationSucceedsWithValidConfiguration()
            throws AlarmCallbackConfigurationException {
        plivoSmsAlarmCallback.initialize(VALID_CONFIGURATION);
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsAuthIdIsMissing()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        plivoSmsAlarmCallback.initialize(new Configuration(ImmutableMap.<String, Object>of(
                "auth_token", TEST_AUTH_TOKEN,
                "src_number", TEST_SRC_NUMBER,
                "dst_number", TEST_DST_NUMBER
        )));
        plivoSmsAlarmCallback.checkConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsAuthTokenIsMissing()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        plivoSmsAlarmCallback.initialize(new Configuration(ImmutableMap.<String, Object>of(
                "auth_id", TEST_AUTH_ID,
                "src_number", TEST_SRC_NUMBER,
                "dst_number", TEST_DST_NUMBER
        )));
        plivoSmsAlarmCallback.checkConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsSrcNumberIsMissing()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        plivoSmsAlarmCallback.initialize(new Configuration(ImmutableMap.<String, Object>of(
                "auth_id", TEST_AUTH_ID,
                "auth_token", TEST_AUTH_TOKEN,
                "dst_number", TEST_DST_NUMBER
        )));
        plivoSmsAlarmCallback.checkConfiguration();
    }

    @Test(expected = ConfigurationException.class)
    public void checkConfigurationFailsDstNumberIsMissing()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        plivoSmsAlarmCallback.initialize(new Configuration(ImmutableMap.<String, Object>of(
                "auth_id", TEST_AUTH_ID,
                "auth_token", TEST_AUTH_TOKEN,
                "src_number", TEST_SRC_NUMBER
        )));
        plivoSmsAlarmCallback.checkConfiguration();
    }

    @Test
    public void testGetRequestedConfiguration()
            throws AlarmCallbackConfigurationException, ConfigurationException {
        assertThat(plivoSmsAlarmCallback.getRequestedConfiguration().asList().keySet(),
                hasItems("auth_id", "auth_token", "src_number", "dst_number"));
    }

    @Test
    public void testPlivoAlarm() throws Exception {
        final StringBuilder descriptionBuilder = new StringBuilder(200);
        when(mockCheckResult.getResultDescription()).thenReturn(descriptionBuilder.toString());

        plivoSmsAlarmCallback.initialize(VALID_CONFIGURATION);
        final LinkedHashMap<String, String> expectedParams = new LinkedHashMap<String, String>();
        expectedParams.put("src", TEST_SRC_NUMBER);
        expectedParams.put("dst", TEST_DST_NUMBER);
        expectedParams.put("text", "Graylog TEST");

        messageResponse = plivoApi.sendMessage(expectedParams);
        assertThat(messageResponse.error, equalTo(null));
    }

    @Test
    public void testGetName() throws Exception {
        assertThat(plivoSmsAlarmCallback.getName(), equalTo("Plivo Sms Alarm CallBack"));
    }

    @Test
    public void testGetAttributes() throws Exception {
        plivoSmsAlarmCallback.initialize(VALID_CONFIGURATION);
        final Map<String, Object> attributes = plivoSmsAlarmCallback.getAttributes();
        assertThat((String) attributes.get("src_number"), equalTo(TEST_SRC_NUMBER));
    }

}