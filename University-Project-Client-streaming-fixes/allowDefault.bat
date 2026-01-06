@echo off
:: Set the proxy server and port
set "proxyServer=127.0.0.1:4444"
setlocal enabledelayedexpansion

:: Read the lines from the input file and concatenate them with semicolons
set "proxyExceptions="
for /f "delims=" %%a in (proxy_exceptions.txt) do (
    set "proxyExceptions=!proxyExceptions!;%%a"
)

:: Remove the leading semicolon
set "proxyExceptions=!proxyExceptions:~1!"

:: Set the proxy exceptions in the Windows Registry
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyOverride /t REG_SZ /d "!proxyExceptions!" /f

endlocal

:: Continue outside the local environment
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyServer /t REG_SZ /d %proxyServer% /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyEnable /t REG_DWORD /d 1 /f
reg add "HKCU\Software\Policies\Microsoft\Internet Explorer\Control Panel" /v Proxy /t REG_DWORD /d 1 /f