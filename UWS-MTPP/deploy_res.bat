@echo off

set base=C:\Work\project\UWS-MTPP

REM picture and lang 
set res_path=C:\Work\project\Resource\uws-mtpp
set res_dest_path=%base%\build\classes\guisanboot\res

REM lib
set lib_path=C:\Work\project\jni-lib\uws-mtpp
set lib_dest_path=%base%

set opt=/s /e /y

xcopy %res_path%  %res_dest_path%\  %opt%

xcopy %lib_path% %lib_dest_path%\ %opt%
