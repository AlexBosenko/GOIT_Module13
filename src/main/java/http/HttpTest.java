package http;

import http.placeholderdata.User;
import http.utils.PlaceHolderUtil;

import java.io.IOException;

public class HttpTest {
    public static void main(String[] args) throws IOException {
        System.out.println("---------- TASK 1 ----------");

        System.out.println("########## create user ############");
        User testUser = PlaceHolderUtil.createTestUser();
        User createdUser = PlaceHolderUtil.createUserApache(testUser);

        System.out.println("########## change user ############");
        User userById6 = PlaceHolderUtil.getUserByIdApache(6);
        userById6.setPhone("999-77-77");
        PlaceHolderUtil.changeUserApache(createdUser, userById6.getId());

        System.out.println("########## delete user ############");
        PlaceHolderUtil.deleteUserApache(5);

        System.out.println("########## get all users ############");
        PlaceHolderUtil.getAllUsersApache();

        System.out.println("########## get user by id ############");
        User userById7 = PlaceHolderUtil.getUserByIdApache(3);

        System.out.println("########## get user by username ############");
        PlaceHolderUtil.getUserByUserNameApache("Elwyn.Skiles");

        System.out.println("---------- TASK 2 ----------");

        int userId = 9;
        int postId = PlaceHolderUtil.getLastPostIdByUser(userId);
        PlaceHolderUtil.getPostsById(userId, postId);

        System.out.println("---------- TASK 3 ----------");

        PlaceHolderUtil.getOpenedTasksByUser(userId);
    }
}
