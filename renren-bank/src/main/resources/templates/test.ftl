<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>Title</title>
</head>
<body>
    <form method="post" action="gateway">
        <input type="hidden" name="serviceName" value="PERSONAL_REGISTER_EXPAND"><br>
        <input type="hidden" name="platformNo" value="202107270001"><br>
        <input type="hidden" name="reqData" value="{&quot;platformUserNo&quot;:100001,&quot;requestNo&quot;:202011110000001,&quot;userRole&quot;:&quot;BORROWERS&quot;,&quot;checkType&quot;:&quot;LIMIT&quot;,&quot;redirectUrl&quot;:&quot;http://localhost:8080/users/createResult&quot;}"><br>
        <input type="hidden" name="sign" value="D9E07CB066CAB5D76BC0DB7AA6FCAD23"><br>
        <input type="hidden" name="keySerial" value="1"><br>
        <input type="submit" value="开通存管账户">
    </form>
</body>
</html>