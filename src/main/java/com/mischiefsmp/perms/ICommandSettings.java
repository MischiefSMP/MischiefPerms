package com.mischiefsmp.perms;

public interface ICommandSettings {
    String HELP_USAGE = "/perms help";
    String HELP_PERMS = "perms.help";

    String GROUP_CREATE_USAGE = "/perms group create <id>";
    String GROUP_CREATE_PERMS = "perms.group.create";

    String GROUP_DELETE_USAGE = "/perms group delete <id>";
    String GROUP_DELETE_PERMS = "perms.group.delete";

    String GROUP_CLEAR_USAGE = "/perms group clear <id>";
    String GROUP_CLEAR_PERMS = "perms.group.clear";

    String GROUP_GRANT_USAGE = "/perms group grant <id> <permission> [world]";
    String GROUP_GRANT_PERMS = "perms.group.grant";

    String GROUP_DENY_USAGE = "/perms group deny <id> <permission> [world]";
    String GROUP_DENY_PERMS = "perms.group.deny";

    String GROUP_REMOVE_USAGE = "/perms group remove <id> <permission> [world]";
    String GROUP_REMOVE_PERMS = "perms.group.remove";

    String GROUP_PREFIX_USAGE = "/perms group prefix <id> <prefix>";
    String GROUP_PREFIX_PERMS = "perms.group.prefix";

    String GROUP_SUFFIX_USAGE = "/perms group suffix <id> <suffix>";
    String GROUP_SUFFIX_PERMS = "perms.group.suffix";


    String USER_CLEAR_USAGE = "/perms user clear <username>";
    String USER_CLEAR_PERMS = "perms.user.clear";

    String USER_GRANT_USAGE = "/perms user grant <username> <permission> [world]";
    String USER_GRANT_PERMS = "perms.user.grant";

    String USER_DENY_USAGE = "/perms user deny <username> <permission> [world]";
    String USER_DENY_PERMS = "perms.user.deny";

    String USER_REMOVE_USAGE = "/perms user remove <username> <permission> [world]";
    String USER_REMOVE_PERMS = "perms.user.remove";

    String USER_PREFIX_USAGE = "/perms user prefix <username> <prefix>";
    String USER_PREFIX_PERMS = "perms.user.prefix";

    String USER_SUFFIX_USAGE = "/perms user suffix <username> <suffix>";
    String USER_SUFFIX_PERMS = "perms.user.suffix";
}
