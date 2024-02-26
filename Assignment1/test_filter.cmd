@echo off
move filter.properties  filter-save.properties
@echo ==== columns=3, precision=13, groups=0 ^(defaults^)> test_results.txt
java Filter  < data.txt >> test_results.txt
@echo:>> test_results.txt
move filter-save.properties  filter.properties
@echo ==== columns=5, precision=6, groups=12 ^(properties^)>> test_results.txt
java Filter  < data.txt >> test_results.txt
@echo:>> test_results.txt
@echo ==== columns=7, precision=6, groups=12 ^(properties, env var^)>> test_results.txt
set CS336_COLUMNS=7
java Filter  < data.txt >> test_results.txt
@echo:>> test_results.txt
@echo ==== columns=8, precision=6, groups=15 ^(cmd-line, env vars^)>> test_results.txt
set CS336_GROUPS=15
java Filter  8  < data.txt >> test_results.txt
@echo:>> test_results.txt
@echo ====  columns=5, precision=2, groups=20 ^(cmd-line^)>> test_results.txt
java Filter  5  2  20  < data.txt >> test_results.txt
@echo:>> test_results.txt
@echo ==== columns=3, precision=4, groups=0 ^(default, env var^)>> test_results.txt
move filter.properties  filter-save.properties
set CS336_COLUMNS=
set CS336_GROUPS=
set CS336_PRECISION=4
java Filter < data.txt >> test_results.txt
@echo:>> test_results.txt
set CS336_PRECISION=
move filter-save.properties  filter.properties
pause
