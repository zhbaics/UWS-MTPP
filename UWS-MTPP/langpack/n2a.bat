::@echo off

set cmd=C:\Program Files\Java\jdk1.5.0_17\bin
set dest=C:\work\project\Resource\uws-mtpp
set dest1=C:\work\project\CheckResource\build\classes\checkresource

"%cmd%\native2ascii" china_sb.txt SBBundle_zh_CN.properties

copy SBBundle_zh_CN.properties  %dest%\SBBundle_zh_CN.properties
copy SBBundle_en_US.properties  %dest%\SBBundle_en_US.properties
copy SBBundle_ja_JP.properties  %dest%\SBBundle_ja_JP.properties

copy SBBundle_zh_CN.properties  %dest1%\SBBundle_zh_CN.properties
copy SBBundle_en_US.properties  %dest1%\SBBundle_en_US.properties
copy SBBundle_ja_JP.properties  %dest1%\SBBundle_ja_JP.properties

set deploy_cmd=C:\work\project\UWS-MTPP

%deploy_cmd%\deploy_res.bat
