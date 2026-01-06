@echo off
:: Remove proxy settings from the Windows Registry
reg delete "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyServer /f
reg delete "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyOverride /f

:: Disable the proxy settings in the Windows Registry and enable user access to proxy settings
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyEnable /t REG_DWORD /d 0 /f
reg delete "HKCU\Software\Policies\Microsoft\Internet Explorer\Control Panel" /v Proxy /f
