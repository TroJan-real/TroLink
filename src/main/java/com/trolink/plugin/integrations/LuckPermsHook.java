package com.trolink.plugin.integrations;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.UUID;

public class LuckPermsHook {
    private final LuckPerms luckPerms;

    public LuckPermsHook() {
        this.luckPerms = LuckPermsProvider.get();
    }

    public void addGroup(UUID uuid, String groupName) {
        luckPerms.getUserManager().modifyUser(uuid, user ->
                user.data().add(InheritanceNode.builder(groupName).build())
        );
    }

    public void removeGroup(UUID uuid, String groupName) {
        luckPerms.getUserManager().modifyUser(uuid, user ->
                user.data().remove(InheritanceNode.builder(groupName).build())
        );
    }

    public boolean hasGroup(UUID uuid, String groupName) {
        User user = luckPerms.getUserManager().getUser(uuid);
        if (user == null) return false;
        return user.getNodes().stream()
                .filter(node -> node instanceof InheritanceNode)
                .map(node -> (InheritanceNode) node)
                .anyMatch(node -> node.getGroupName().equalsIgnoreCase(groupName));
    }
}
