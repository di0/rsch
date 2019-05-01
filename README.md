# The Remote Search utility

The rsch(Remote Search) app is an utility that allow you to make remote searches according given one or more keyword pattern.

# How it work

You can run the **rsch** command following way:

```bash
rsch -s 192.168.0.1 -u foo -p foo123 "keyword" /directory-dst/
```
where:

**-s** parameter is the IP/Hostname from server where the search will be done.
**-u** parameter is the user to the remote server.
**-p** parameter is the password to the remote server.
**keyword** argument is the keyword match to the given pattern.
**/directory-dst/(or filename)** argument is the directory or filename that contains
the keyword.
