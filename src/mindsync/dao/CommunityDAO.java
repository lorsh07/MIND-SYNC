package mindsync.dao;
import mindsync.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

import mindsync.model.CommunityComment;
import mindsync.model.CommunityPost;

public class CommunityDAO {
    public boolean createPost(String patientId, String title, String content, String createdAt, boolean anonymous) {
        String id = UUID.randomUUID().toString();
        String sql = "INSERT INTO communityPosts(id, patientId,title, content,createdAt,anonymous)" +
                " VALUES (?, ?, ?, ?, ?,?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, patientId);
            pstmt.setString(3, title);
            pstmt.setString(4, content);
            pstmt.setString(5, createdAt);
            pstmt.setBoolean(6, anonymous);

            pstmt.executeUpdate();
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CommunityPost> getAllPosts() {
        String sql = "SELECT * FROM communityPosts";
        List<CommunityPost> posts = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String patientId = rs.getString("patientId");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String createdAt = rs.getString("createdAt");
                boolean anonymous = rs.getBoolean("anonymous");

                CommunityPost c = new CommunityPost(id, patientId, title, content, createdAt, anonymous);
                posts.add(c);
            }
            return posts;
        }catch (SQLException e) {
            e.printStackTrace();
            return posts;
        }
    }

    public boolean createComment(String postId,String patientId,String content, String createdAt, boolean anonymous) {
        String id = UUID.randomUUID().toString();
        String sql =  "INSERT INTO communityComments(id, postId, patientId, content, createdAt,anonymous) "+
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,id );
            pstmt.setString(2, postId);
            pstmt.setString(3, patientId);
            pstmt.setString(4, content);
            pstmt.setString(5, createdAt);
            pstmt.setBoolean(6, anonymous);

            pstmt.executeUpdate();
            return true;


        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CommunityComment> getCommentsByPostId (String postId) {
        String sql = "SELECT * FROM communityComments WHERE postId = ?";
        List<CommunityComment> comments = new ArrayList<>();

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postId);
            ResultSet rs =  pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String pId = rs.getString("postId");
                String patientId = rs.getString("patientId");
                String content = rs.getString("content");
                String createdAt = rs.getString("createdAt");
                boolean anonymous = rs.getBoolean("anonymous");

                CommunityComment cc = new CommunityComment(id, pId, patientId, content, createdAt, anonymous);
                comments.add(cc);
            }
            return comments;

        }catch (SQLException e){
            e.printStackTrace();
            return comments;
        }
    }
}