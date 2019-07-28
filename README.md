# Study Session Tracker and Statistics Application

This application consists of two main functionality:
- It allows the user to track study sessions over the course of a semester.
- It prints statistics about these study sessions.


### Documentation  
```
Usage: VivePassion Statistics [list|start|stop] [options]

  --help                  prints this usage text
  --version               

The program is in analysing mode by default. This means that you'll get
 analysed output if a command is invoked. For other functionality please type
 the corresponding command.

List of options in analysing mode:

  -a, --alone             total study time alone (default)
  -g, --group             total study time in a group
  --debug                 activate debug messages

Available commands plus options:

Command: list [options]
list all learn sessions
  -a, --alone             list study sessions done alone
  -g, --group             list study sessions done in a group
Command: start
start a new study session
Command: stop
stop the current study session.
 The application will ask for the form of learning and the course.
```