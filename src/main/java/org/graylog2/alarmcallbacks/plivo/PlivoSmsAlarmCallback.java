package org.graylog2.alarmcallbacks.plivo;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.message.MessageResponse;
import com.plivo.helper.exception.PlivoException;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.TextField;
import org.graylog2.plugin.streams.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;


public class PlivoSmsAlarmCallback implements AlarmCallback {

    private static final Logger LOG = LoggerFactory.getLogger(PlivoSmsAlarmCallback.class);
    private static final String CK_AUTH_ID = "auth_id";
    private static final String CK_AUTH_TOKEN = "auth_token";
    private static final String CK_SRC_NUMBER = "src_number";
    private static final String CK_DST_NUMBER = "dst_number";

    private static final String[] MANDATORY_CONFIGURATION_KEYS = new String[]{
            CK_AUTH_ID, CK_AUTH_TOKEN, CK_SRC_NUMBER, CK_DST_NUMBER
    };

    private Configuration configuration;

    @Override
    public void initialize(final Configuration config) throws AlarmCallbackConfigurationException {
        this.configuration = config;

        try {
            checkConfiguration();
        } catch (ConfigurationException e) {
            LOG.error("Check Configuration Error -> " + e.getMessage());
        }
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult checkResult) throws AlarmCallbackException {
        try {
            send(checkResult);
        } catch (PlivoException e) {
            LOG.error("Plivo Api Fail -> " + e.getMessage());
        }
    }


    @Override
    public ConfigurationRequest getRequestedConfiguration() {
        final ConfigurationRequest configurationRequest = new ConfigurationRequest();

        // Auth ID
        configurationRequest.addField(new TextField(CK_AUTH_ID, "Auth ID", "", "Plivo Auth ID",
                ConfigurationField.Optional.NOT_OPTIONAL));

        // Auth Token
        configurationRequest.addField(new TextField(CK_AUTH_TOKEN, "Auth Token", "", "Plivo Auth Token",
                ConfigurationField.Optional.NOT_OPTIONAL));

        // Source Phone Number
        configurationRequest.addField(new TextField(CK_SRC_NUMBER, "Source Number", "", "Plivo Source Phone Number",
                ConfigurationField.Optional.NOT_OPTIONAL));

        // Destination Phone Number
        configurationRequest.addField(new TextField(CK_DST_NUMBER, "Destination Number", "", "Plivo Destination Phone Number",
                ConfigurationField.Optional.NOT_OPTIONAL));
        return configurationRequest;
    }

    @Override
    public String getName() {
        return "Plivo Sms Alarm CallBack";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return configuration.getSource();
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {
        for (String key : MANDATORY_CONFIGURATION_KEYS) {
            if (!configuration.stringIsSet(key)) {
                throw new ConfigurationException(key + " is mandatory and must not be empty.");
            }
        }
    }

    public void send(AlertCondition.CheckResult result) throws PlivoException {
        RestAPI plivoApi = new RestAPI(configuration.getString(CK_AUTH_ID), configuration.getString(CK_AUTH_TOKEN), "v1");

        final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("src", configuration.getString(CK_SRC_NUMBER));
        params.put("dst", configuration.getString(CK_DST_NUMBER));
        params.put("text", "Graylog " + result.getResultDescription());

        try {
            MessageResponse messageResponse = plivoApi.sendMessage(params);
            LOG.debug("Sending Sms -> ", messageResponse.toString());
        } catch (PlivoException e) {
            LOG.error("Error Sending Sms -> ", e.getMessage());
        }
    }
}
