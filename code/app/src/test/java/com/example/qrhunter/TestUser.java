package com.example.qrhunter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestUser {

    private User testUser(String username, String password, String email, String phoneNum) {
        return new User(username, password, email, phoneNum);
    }

    @Test
    void testUser() {
        User u = testUser("testname", "testpass", "testemail", "testPhone123");
        assertEquals("testname", u.getUsername());
        assertEquals(null, u.getUniqueQRHash());
        assertEquals(0, u.getScannedQRCodes().size());
    }


}
