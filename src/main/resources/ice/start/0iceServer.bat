::隐藏窗口运行bat文件
echo CreateObject("WScript.Shell").Run "cmd /c G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\start\1start.bat",0>$tmp.vbs
cscript.exe /e:vbscript $tmp.vbs
del $tmp.vbs