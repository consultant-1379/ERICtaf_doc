@echo off
rem This is just a convenience script for developers only, you are welcome to write your own .sh.
pelican
cd output
echo Please open http://localhost:8000/
python -m pelican.server
