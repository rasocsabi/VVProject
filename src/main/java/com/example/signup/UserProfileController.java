package com.example.signup;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserProfileController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label groupLabel;

    @FXML
    private ListView<String> skillsListView;

    @FXML
    private Button editNameButton;

    @FXML
    private Button editGroupButton;

    @FXML
    private Button addSkillButton;

    @FXML
    private Button removeSkillButton;

    @FXML
    private Button addSkillLevelButton;

    @FXML
    private Button setCostButton;

    private User user;

    public void setUser(User user) {
        this.user = user;
        updateProfile();
    }

    private void updateProfile() {
        nameLabel.setText(user.getUsername());
        groupLabel.setText(user.getGroupName());
        skillsListView.setItems(user.getSkills());
    }

    @FXML
    private void handleEditNameButton() {
        TextInputDialog dialog = new TextInputDialog(user.getUsername());
        dialog.setTitle("Edit Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new name:");
        dialog.showAndWait().ifPresent(newName -> {
            user.setName(newName);
            updateProfile();
        });
    }

    @FXML
    private void handleEditGroupButton() {
        TextInputDialog dialog = new TextInputDialog(user.getGroupName());
        dialog.setTitle("Edit Group");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new group:");
        dialog.showAndWait().ifPresent(newGroup -> {
            user.setGroup(newGroup);
            updateProfile();
        });
    }

    @FXML
    private void handleAddSkillButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Skill");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter skill name:");
        dialog.showAndWait().ifPresent(skillName -> {
            user.addSkill(skillName);
            updateProfile();
        });
    }

    @FXML
    private void handleRemoveSkillButton() {
        String selectedSkill = skillsListView.getSelectionModel().getSelectedItem();
        if (selectedSkill != null) {
            user.removeSkill(selectedSkill);
            updateProfile();
        }
    }

    @FXML
    private void handleAddSkillLevelButton() {
        String selectedSkill = skillsListView.getSelectionModel().getSelectedItem();
        if (selectedSkill != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Set Skill Level");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter skill level for " + selectedSkill + ":");
            dialog.showAndWait().ifPresent(skillLevel -> {
                user.setSkillLevel(selectedSkill, skillLevel);
                updateProfile();
            });
        }
    }

    @FXML
    private void handleSetCostButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set Cost");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter cost (hourly rate):");
        dialog.showAndWait().ifPresent(cost -> {
            user.setCost(cost);
            updateProfile();
        });
    }
}
