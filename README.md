[![Codacy Badge](https://api.codacy.com/project/badge/Grade/39e4bc19d5fa426a945ab2d285baebbd)](https://www.codacy.com/app/KrokodileDandy/study-session-statistics?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=KrokodileDandy/study-session-statistics&amp;utm_campaign=Badge_Grade)

# Study Session Tracker and Statistics Application

This application consists of two main functionality:
  * It allows the user to track study sessions over the course of a studyForm.
  * It prints statistics about these study sessions.

# Quick Start  
The general format for using the program is `vpstat [command] [options]`.  

The program is in analysing mode by default. This means invoking an option without
a command like this: `vpstat [options]` will have the same effect as invoking
`vpstat analyze [options]`.

Available commands are:  
```
  list                     list all study sessions
  start                    start a new study session
  pause                    pause the current study session
  resume                   resume the current study session
  stop                     stop the current study session
```

# Documentation
## Global options  
```
  --help                   prints this usage text
  --version
  --debug                  activate debug messages
```

## Options in analyzing mode  
```
  -a, --alone              total study time alone (default)
  -g, --group              total study time in a group
```

## Available commands plus options
```
Command: list [options]
list all learn sessions
  -a, --alone              list study sessions done alone
  -g, --group              list study sessions done in a group

Command: start [options]
start a new study session
  -f, --form <value>       specify the form of studying
  -c, --course <value>     university course
  -b, --begin <value>      override the begin time of the study session
  -c, --comment <value>    optional comment for the study session
  --studyForm <value>      the studyForm of the study session (if not given, the last studyForm of the last registered studyForm is used)

Command: pause
pause the current study session

Command: resume
resume the paused study session

Command: stop
stop the current study session.
 The application will then ask for the form of learning and the course.
```
