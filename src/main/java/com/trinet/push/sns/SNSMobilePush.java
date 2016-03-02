package com.trinet.push.sns;

/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.trinet.push.sns.SampleMessageGenerator.Platform;

public class SNSMobilePush {

	private AmazonSNSClientWrapper snsClientWrapper;

	public SNSMobilePush(AmazonSNS snsClient) {
		this.snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
	}

	public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
	static {
		attributesMap.put(Platform.ADM, null);
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
		attributesMap.put(Platform.APNS_SANDBOX, null);
		attributesMap.put(Platform.BAIDU, addBaiduNotificationAttributes());
		attributesMap.put(Platform.WNS, addWNSNotificationAttributes());
		attributesMap.put(Platform.MPNS, addMPNSNotificationAttributes());
	}

	public static void main(String[] args) throws IOException {
		/*
		 * TODO: Be sure to fill in your AWS access credentials in the
		 * AwsCredentials.properties file before you try to run this sample.
		 * http://aws.amazon.com/security-credentials
		 */
		AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(
				SNSMobilePush.class
						.getResourceAsStream("AwsCredentials.properties")));

		sns.setEndpoint("https://sns.us-west-2.amazonaws.com");
		System.out.println("===========================================\n");
		System.out.println("Getting Started with Amazon SNS");
		System.out.println("===========================================\n");
		try {
			SNSMobilePush sample = new SNSMobilePush(sns);
			/* TODO: Uncomment the services you wish to use. */
			//sample.demoAndroidAppNotification();
			// sample.demoKindleAppNotification();
			 //sample.demoAppleAppNotification();
		sample.demoAppleSandboxAppNotification();
			// sample.demoBaiduAppNotification();
			// sample.demoWNSAppNotification();
			// sample.demoMPNSAppNotification();
		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon SNS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with SNS, such as not "
							+ "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	public void demoAndroidAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAndroidMessage()
		String serverAPIKey = "AIzaSyAiSUT1-7q7fUbezwtnwCLFcmKWAFHngCw";
		String applicationName = "TriNetMobileAlpha";
		String registrationId = "APA91bHTrg-flL1ENaYd2Zi7SKaGPF5_S2aMIufnCYL9OdjO4wq6kmsZxS4nr0lvXk7OEAaR-cmNB7ZrIl8L8Tqs9j2AhZ4_626tIZLpUcQvoBZkcsiilq4OuWTXU-D1ho4WsFWI51q1";
		snsClientWrapper.demoNotification(Platform.GCM, "", serverAPIKey,registrationId, applicationName, attributesMap);
	}

	public void demoKindleAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleKindleMessage()
		String clientId = "";
		String clientSecret = "";
		String applicationName = "";

		String registrationId = "";
		snsClientWrapper.demoNotification(Platform.ADM, clientId, clientSecret,
				registrationId, applicationName, attributesMap);
	}

	public void demoAppleAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()
		String certificate = "-----BEGIN CERTIFICATE-----MIIFizCCBHOgAwIBAgIIK6aJGP6ejZkwDQYJKoZIhvcNAQEFBQAwgZYxCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBwbGUgV29ybGR3aWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTUwMjA5MDAxMjQwWhcNMTYwMjA5MDAxMjQwWjCBijEjMCEGCgmSJomT8ixkAQEME2NvbS50cmluZXQucHVzaGNoYXQxQTA/BgNVBAMMOEFwcGxlIERldmVsb3BtZW50IElPUyBQdXNoIFNlcnZpY2VzOiBjb20udHJpbmV0LnB1c2hjaGF0MRMwEQYDVQQLDAo1OEhLWkc4WVZWMQswCQYDVQQGEwJVUzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMVrkCz5e2kmkqqLMjFU+ZYXeuFOB7JHCys9WXlV06P61a6FTGMmqL7u1OaY28JFhvXXkHynOp4D50/3bAMY7HMh57MqsXCh09bD5PLg03n+xo5mECEH8ti/4G2qDC/ybUgpIP8US1B7FYkYd3+0FSQMjeYOLlOx8f1G8XGbqdJxTl51OnwhyZgN6qfCz+S9vfgfsqeOU5OsL/pk6sTySQYaZAKw9bV0FwKSKqzVDZfyLbeKZUuHuGLGkVmVUySWi2tnme3D/gdThxPQBQnVv4fVTv9x7IGSJdFExmHXfnf04EhyMqOqsl0dIfE7pK3dqDHbApv7QsE4msv6bcSiwfUCAwEAAaOCAeUwggHhMB0GA1UdDgQWBBQjZAD7oDUmJXaYH+p8iFvch0WT7DAJBgNVHRMEAjAAMB8GA1UdIwQYMBaAFIgnFwmpthhgi+zruvZHWcVSVKO3MIIBDwYDVR0gBIIBBjCCAQIwgf8GCSqGSIb3Y2QFATCB8TCBwwYIKwYBBQUHAgIwgbYMgbNSZWxpYW5jZSBvbiB0aGlzIGNlcnRpZmljYXRlIGJ5IGFueSBwYXJ0eSBhc3N1bWVzIGFjY2VwdGFuY2Ugb2YgdGhlIHRoZW4gYXBwbGljYWJsZSBzdGFuZGFyZCB0ZXJtcyBhbmQgY29uZGl0aW9ucyBvZiB1c2UsIGNlcnRpZmljYXRlIHBvbGljeSBhbmQgY2VydGlmaWNhdGlvbiBwcmFjdGljZSBzdGF0ZW1lbnRzLjApBggrBgEFBQcCARYdaHR0cDovL3d3dy5hcHBsZS5jb20vYXBwbGVjYS8wTQYDVR0fBEYwRDBCoECgPoY8aHR0cDovL2RldmVsb3Blci5hcHBsZS5jb20vY2VydGlmaWNhdGlvbmF1dGhvcml0eS93d2RyY2EuY3JsMAsGA1UdDwQEAwIHgDATBgNVHSUEDDAKBggrBgEFBQcDAjAQBgoqhkiG92NkBgMBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAn62EXt3HY8v3Ae2HaomjL1/9Vca+Apb9QM0PTNrI0ggJeuMLouYGG/BywGJrkfQKmnPcebBaItv0/Dmt2jtxTNyeEUMWxsTxAGQg3bDsjbddtiOOkcJPxENGz1dxLw49jzVwuxj0xt7fRhhCNM1GDssTJ3SP9z3DaKXfU62DZw5lvGZrC/tw0ioznFQ0F9cGEa43roiCL1D9yLyrZ57IT8BOc54VGFR05igDFXUNA1Uk++GPHy7nocmsXnsINMGTi9pcHvx2Fzo4cTaPU10CH0PKyBUkwg2SmqedjcY7aZOlTb6XHgeQEnxdNLeJ+d37hu6XMkyNgUuhyEWZD79TWQ==-----END CERTIFICATE-----"; // This should be in pem format with \n at the// end of each line.
		String privateKey = "-----BEGIN RSA PRIVATE KEY-----MIIEpQIBAAKCAQEAxWuQLPl7aSaSqosyMVT5lhd64U4HskcLKz1ZeVXTo/rVroVMYyaovu7U5pjbwkWG9deQfKc6ngPnT/dsAxjscyHnsyqxcKHT1sPk8uDTef7GjmYQIQfy2L/gbaoML/JtSCkg/xRLUHsViRh3f7QVJAyN5g4uU7Hx/UbxcZup0nFOXnU6fCHJmA3qp8LP5L29+B+yp45Tk6wv+mTqxPJJBhpkArD1tXQXApIqrNUNl/Itt4plS4e4YsaRWZVTJJaLa2eZ7cP+B1OHE9AFCdW/h9VO/3HsgZIl0UTGYdd+d/TgSHIyo6qyXR0h8Tukrd2oMdsCm/tCwTiay/ptxKLB9QIDAQABAoIBADyvzgglQOyX8OoAFBPaHUcoPJvZ0r/Y2vQVzdVgrshvqRQW4d5w2dqnrEaeop9bpBsags3u0jQU2xxX4JorJTRXLdSDwTvwYyBX459NwXR0/zBy625b9SjwAttzNH/gs/tEuWHwKR66V2pnXQSipQUJ3uzjWxZbUxoansnfohqQMzyy+ZeLf7cU+deMqwFU3KygY9uu8YsX8tmEAAkBUUI8uLhV4WluR9yzZdUwpddyw3PHVKNwcuEs4lZecWVPcnDwyis+tBe1Iqs5ln/sMkNeWZ3xYhMS8zmit7dixKybB6xDFcWKoVMY0Kxxv8i+5w/hYVqZDGshYJMSEb5QHpkCgYEA958mIDDmDwjTHkYe1TrZ29jk5gwy3q3ZfugcHkeRCFv2IvCm5r9gF6PSJmA4ozZSDNXw0FeTpsGKf4Xr5jrbpmQMYPJb8oiTwk34QRhfiZdr5E3zcUCDeJJPa/yMBcVpoWHRav57rML360obe3ogD+U+1RrolfZwbVuh8vvfnQ8CgYEAzBmUGWxVorAbc1kLQavzT08fWG+13aFc5XUcR0Ea4GZ2jzthicxTd2pmOH49Rqw8Feu0dw5E24Bz9lZ8UVdogQsTnax7v+G/j3/fnzm7T29zcWYnIbaPkz1OpHO/3ncG6LQ4tQitHhRpodXH/sdw2Hx8PdIQUuycd+XHGTaUeLsCgYEA0E/NhhU85Pb6jCRXOfCpaB6d6b3naZd9OhrP1NVF+G9MsHYdIk9q6lhBqVEXNI61FeFBFjQmnYmCFue13Vg0glT2aPRaT71/+F0ZdMGpVZnSBR1iqCqqr1r+f4zUyuIZi887mrP9nQfkAse0OPszT8v+QM7DTa0lRYppwn0L0BcCgYEAu5aK2y8KKmQoZsUhpnNvRqaXTcYm5pQn+LHBkvAQzsz+DTlrtaY51NjTKPtGQI2PECq6jG5t9T1D19a3nl9zKp/OoK3mc37+fNrv4r3CHgl2lnRoZQERRygi/t1wLP1DOWBjemiGDM6AJAGENvSCEEeiPYO0AzN7wGtNqivTHH8CgYEAqWHNlDB82btKG+caQ7Nd8h81ydRNC242BjDO01TZBfyZVcX/4Y0bzUh3nS8QW4zD4yUp/kn3LBwF8y9WH2vm6RXodX++IocAVC5sSX0STWAkIdFdJfq9QLbL2T3AKEDggk4jo0vg7yhzIzLlkNLfn+sakhL+MF5IXFnoaMJ9EAU=-----END RSA PRIVATE KEY-----";; // This should be in pem format with \n at the
								// end of each line.
		String applicationName = "TriNetMobileDev";
		String deviceToken = "a34e8238946a84c8d21ed016c0df96cb4002c4fc1874c93a22543279f4bc0d83"; // This is 64 hex characters.
		snsClientWrapper.demoNotification(Platform.APNS, certificate,
				privateKey, deviceToken, applicationName, attributesMap);
	}

	public void demoAppleSandboxAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()
		String certificate = "-----BEGIN CERTIFICATE-----\nMIIFizCCBHOgAwIBAgIIK6aJGP6ejZkwDQYJKoZIhvcNAQEFBQAwgZYxCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBwbGUgV29ybGR3aWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTUwMjA5MDAxMjQwWhcNMTYwMjA5MDAxMjQwWjCBijEjMCEGCgmSJomT8ixkAQEME2NvbS50cmluZXQucHVzaGNoYXQxQTA/BgNVBAMMOEFwcGxlIERldmVsb3BtZW50IElPUyBQdXNoIFNlcnZpY2VzOiBjb20udHJpbmV0LnB1c2hjaGF0MRMwEQYDVQQLDAo1OEhLWkc4WVZWMQswCQYDVQQGEwJVUzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMVrkCz5e2kmkqqLMjFU+ZYXeuFOB7JHCys9WXlV06P61a6FTGMmqL7u1OaY28JFhvXXkHynOp4D50/3bAMY7HMh57MqsXCh09bD5PLg03n+xo5mECEH8ti/4G2qDC/ybUgpIP8US1B7FYkYd3+0FSQMjeYOLlOx8f1G8XGbqdJxTl51OnwhyZgN6qfCz+S9vfgfsqeOU5OsL/pk6sTySQYaZAKw9bV0FwKSKqzVDZfyLbeKZUuHuGLGkVmVUySWi2tnme3D/gdThxPQBQnVv4fVTv9x7IGSJdFExmHXfnf04EhyMqOqsl0dIfE7pK3dqDHbApv7QsE4msv6bcSiwfUCAwEAAaOCAeUwggHhMB0GA1UdDgQWBBQjZAD7oDUmJXaYH+p8iFvch0WT7DAJBgNVHRMEAjAAMB8GA1UdIwQYMBaAFIgnFwmpthhgi+zruvZHWcVSVKO3MIIBDwYDVR0gBIIBBjCCAQIwgf8GCSqGSIb3Y2QFATCB8TCBwwYIKwYBBQUHAgIwgbYMgbNSZWxpYW5jZSBvbiB0aGlzIGNlcnRpZmljYXRlIGJ5IGFueSBwYXJ0eSBhc3N1bWVzIGFjY2VwdGFuY2Ugb2YgdGhlIHRoZW4gYXBwbGljYWJsZSBzdGFuZGFyZCB0ZXJtcyBhbmQgY29uZGl0aW9ucyBvZiB1c2UsIGNlcnRpZmljYXRlIHBvbGljeSBhbmQgY2VydGlmaWNhdGlvbiBwcmFjdGljZSBzdGF0ZW1lbnRzLjApBggrBgEFBQcCARYdaHR0cDovL3d3dy5hcHBsZS5jb20vYXBwbGVjYS8wTQYDVR0fBEYwRDBCoECgPoY8aHR0cDovL2RldmVsb3Blci5hcHBsZS5jb20vY2VydGlmaWNhdGlvbmF1dGhvcml0eS93d2RyY2EuY3JsMAsGA1UdDwQEAwIHgDATBgNVHSUEDDAKBggrBgEFBQcDAjAQBgoqhkiG92NkBgMBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAn62EXt3HY8v3Ae2HaomjL1/9Vca+Apb9QM0PTNrI0ggJeuMLouYGG/BywGJrkfQKmnPcebBaItv0/Dmt2jtxTNyeEUMWxsTxAGQg3bDsjbddtiOOkcJPxENGz1dxLw49jzVwuxj0xt7fRhhCNM1GDssTJ3SP9z3DaKXfU62DZw5lvGZrC/tw0ioznFQ0F9cGEa43roiCL1D9yLyrZ57IT8BOc54VGFR05igDFXUNA1Uk++GPHy7nocmsXnsINMGTi9pcHvx2Fzo4cTaPU10CH0PKyBUkwg2SmqedjcY7aZOlTb6XHgeQEnxdNLeJ+d37hu6XMkyNgUuhyEWZD79TWQ==\n-----END CERTIFICATE-----"; // This should be in pem format with \n at the// end of each line.
		String privateKey = "-----BEGIN RSA PRIVATE KEY-----\nMIIEpQIBAAKCAQEAxWuQLPl7aSaSqosyMVT5lhd64U4HskcLKz1ZeVXTo/rVroVMYyaovu7U5pjbwkWG9deQfKc6ngPnT/dsAxjscyHnsyqxcKHT1sPk8uDTef7GjmYQIQfy2L/gbaoML/JtSCkg/xRLUHsViRh3f7QVJAyN5g4uU7Hx/UbxcZup0nFOXnU6fCHJmA3qp8LP5L29+B+yp45Tk6wv+mTqxPJJBhpkArD1tXQXApIqrNUNl/Itt4plS4e4YsaRWZVTJJaLa2eZ7cP+B1OHE9AFCdW/h9VO/3HsgZIl0UTGYdd+d/TgSHIyo6qyXR0h8Tukrd2oMdsCm/tCwTiay/ptxKLB9QIDAQABAoIBADyvzgglQOyX8OoAFBPaHUcoPJvZ0r/Y2vQVzdVgrshvqRQW4d5w2dqnrEaeop9bpBsags3u0jQU2xxX4JorJTRXLdSDwTvwYyBX459NwXR0/zBy625b9SjwAttzNH/gs/tEuWHwKR66V2pnXQSipQUJ3uzjWxZbUxoansnfohqQMzyy+ZeLf7cU+deMqwFU3KygY9uu8YsX8tmEAAkBUUI8uLhV4WluR9yzZdUwpddyw3PHVKNwcuEs4lZecWVPcnDwyis+tBe1Iqs5ln/sMkNeWZ3xYhMS8zmit7dixKybB6xDFcWKoVMY0Kxxv8i+5w/hYVqZDGshYJMSEb5QHpkCgYEA958mIDDmDwjTHkYe1TrZ29jk5gwy3q3ZfugcHkeRCFv2IvCm5r9gF6PSJmA4ozZSDNXw0FeTpsGKf4Xr5jrbpmQMYPJb8oiTwk34QRhfiZdr5E3zcUCDeJJPa/yMBcVpoWHRav57rML360obe3ogD+U+1RrolfZwbVuh8vvfnQ8CgYEAzBmUGWxVorAbc1kLQavzT08fWG+13aFc5XUcR0Ea4GZ2jzthicxTd2pmOH49Rqw8Feu0dw5E24Bz9lZ8UVdogQsTnax7v+G/j3/fnzm7T29zcWYnIbaPkz1OpHO/3ncG6LQ4tQitHhRpodXH/sdw2Hx8PdIQUuycd+XHGTaUeLsCgYEA0E/NhhU85Pb6jCRXOfCpaB6d6b3naZd9OhrP1NVF+G9MsHYdIk9q6lhBqVEXNI61FeFBFjQmnYmCFue13Vg0glT2aPRaT71/+F0ZdMGpVZnSBR1iqCqqr1r+f4zUyuIZi887mrP9nQfkAse0OPszT8v+QM7DTa0lRYppwn0L0BcCgYEAu5aK2y8KKmQoZsUhpnNvRqaXTcYm5pQn+LHBkvAQzsz+DTlrtaY51NjTKPtGQI2PECq6jG5t9T1D19a3nl9zKp/OoK3mc37+fNrv4r3CHgl2lnRoZQERRygi/t1wLP1DOWBjemiGDM6AJAGENvSCEEeiPYO0AzN7wGtNqivTHH8CgYEAqWHNlDB82btKG+caQ7Nd8h81ydRNC242BjDO01TZBfyZVcX/4Y0bzUh3nS8QW4zD4yUp/kn3LBwF8y9WH2vm6RXodX++IocAVC5sSX0STWAkIdFdJfq9QLbL2T3AKEDggk4jo0vg7yhzIzLlkNLfn+sakhL+MF5IXFnoaMJ9EAU=\n-----END RSA PRIVATE KEY-----";; // This should be in pem format with \n at the
								// end of each line.
		String applicationName = "TriNetMobileAlpha";
		String deviceToken = "a34e8238946a84c8d21ed016c0df96cb4002c4fc1874c93a22543279f4bc0d83"; // This is 64 hex characters.
		snsClientWrapper.demoNotification(Platform.APNS_SANDBOX, certificate,
				privateKey, deviceToken, applicationName, attributesMap);
	}

	public void demoBaiduAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your Baidu notification, you can
		 * do so by modifying the attribute values in the method
		 * addBaiduNotificationAttributes() . You can also change the
		 * notification payload as per your preferences using the method
		 * com.amazonaws
		 * .sns.samples.tools.SampleMessageGenerator.getSampleBaiduMessage()
		 */
		String userId = "";
		String channelId = "";
		String apiKey = "";
		String secretKey = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.BAIDU, apiKey, secretKey,
				channelId + "|" + userId, applicationName, attributesMap);
	}

	public void demoWNSAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your WNS notification, you can
		 * do so by modifying the attribute values in the method
		 * addWNSNotificationAttributes() . You can also change the notification
		 * payload as per your preferences using the method
		 * com.amazonaws.sns.samples
		 * .tools.SampleMessageGenerator.getSampleWNSMessage()
		 */
		String notificationChannelURI = "";
		String packageSecurityIdentifier = "";
		String secretKey = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.WNS,
				packageSecurityIdentifier, secretKey, notificationChannelURI,
				applicationName, attributesMap);
	}

	public void demoMPNSAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your MPNS notification, you can
		 * do so by modifying the attribute values in the method
		 * addMPNSNotificationAttributes() . You can also change the
		 * notification payload as per your preferences using the method
		 * com.amazonaws
		 * .sns.samples.tools.SampleMessageGenerator.getSampleMPNSMessage ()
		 */
		String notificationChannelURI = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.MPNS, "", "",
				notificationChannelURI, applicationName, attributesMap);
	}

	private static Map<String, MessageAttributeValue> addBaiduNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.DeployStatus",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("1"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageKey",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("default-channel-msg-key"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageType",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("0"));
		return notificationAttributes;
	}

	private static Map<String, MessageAttributeValue> addWNSNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.WNS.CachePolicy",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("cache"));
		notificationAttributes.put("AWS.SNS.MOBILE.WNS.Type",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("wns/badge"));
		return notificationAttributes;
	}

	private static Map<String, MessageAttributeValue> addMPNSNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.MPNS.Type",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("token")); // This attribute is required.
		notificationAttributes.put("AWS.SNS.MOBILE.MPNS.NotificationClass",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("realtime")); // This attribute is required.
														
		return notificationAttributes;
	}
}
