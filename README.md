# P1000SDK
This is a sample app for UCube SDK.

### Add the below line of code in your project's root level gradle.
    maven { url 'https://jitpack.io' }

 e.g 
 ```
 allprojects { 
  repositories {
          google()
          jcenter()
          maven { url 'https://jitpack.io' }
        }
  }
  ```
### Add the below dependencies in the project's build.gradle 

implementation 'com.github.sil-dev:P1000SDK:V2.0.3'

e.g
```
    dependencies {
		implementation 'com.github.sil-dev:P1000SDK:V2.0.3'
	}
 ```

### Add the below permission in the manifest file 

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE " />

### Implementation
1. Initialize the UCubeManager with the context and your provided License-Key.
e.g
```
P1000Manager p1000Manager = P1000Manager.getInstance(Context,License-key);
```
2. Create an Object of P1000Request and set the respective fields:
```
   P1000Request p1000Request = new P1000Request();
                p1000Request.setUsername("username");
                p1000Request.setPassword("password");
                p1000Request.setRefCompany("SIL");
                p1000Request.setMid("mid");
                p1000Request.setTid("tid");
                p1000Request.setTransactionId(p1000Manager.getTransactionId());
                p1000Request.setImei("imei");
                p1000Request.setImsi("null");
                p1000Request.setTxn_amount("0.0");
                p1000Request.setRequestCode(TransactionType.INQUIRY);
```
Provided Transaction type are as follows:
```
    	TransactionType.INQUIRY 	: for balance enquiry
    	TransactionType.WITHDRAWAL	: for amount withdrawal
    	TransactionType.DEBIT		: for sale by card.
```
Transaction Id is your unique 12 digit Id. In case you dont have any such Unique Id , you can call the below method to generate the Unique Transaction Id.<br/>
note: The Transaction Id is returned in the Response Callback i.e successCallback and/or failureCallback as token.
```
p1000Manager.getTransactionId()
```
3. Once the p1000Request object is created and set proceed with the excecution.
e.g 
```
	p1000Manager.execute(uCubeRequest,new UCubeCallBacks() {
            @Override
            public void successCallback(JSONObject jsonObject) {
                
            }

            @Override
            public void progressCallback(String s) {
              
            }

            @Override
            public void failureCallback(JSONObject jsonObject) {
               
            }
        });
```
4. P1000Manager implement the method for success, failure and progress state.
```
	
	i. 	**public void successCallback(JSONObject jsonObject);** <br>
			The above method is called when the transaction is success. 

 	ii. **public void failureCallback(JSONObject jsonObject);** <br>
 			The above method is called when the transaction is failed due to some issues.

 	iii. **public void progressCallback(String message)** <br>
 			
```

**For Reference Then Download this Code**
```
https://drive.google.com/drive/folders/1uE2MYwwAHuZyvmB6UMW9hVnSyuRg14Eh?usp=sharing
```
