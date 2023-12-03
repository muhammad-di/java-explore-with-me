package ru.practicum.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "endpoint_hit")
public class EndpointHitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "app")
    private String app;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "hit_time")
    private LocalDateTime hitTime;
}

//@Entity
//@Table(name = "endpoint_hit")
//public class EndpointHitEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column(name = "app")
//    private String app;
//
//    @Column(name = "uri")
//    private String uri;
//
//    @Column(name = "ip")
//    private String ip;
//
//    @Column(name = "hit_time")
//    private LocalDateTime hitTime;
//
//    public EndpointHitEntity(long id, String app, String uri, String ip, LocalDateTime hitTime) {
//        this.id = id;
//        this.app = app;
//        this.uri = uri;
//        this.ip = ip;
//        this.hitTime = hitTime;
//    }
//
//    public EndpointHitEntity() {
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        EndpointHitEntity entity = (EndpointHitEntity) o;
//        return id == entity.id;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getApp() {
//        return app;
//    }
//
//    public void setApp(String app) {
//        this.app = app;
//    }
//
//    public String getUri() {
//        return uri;
//    }
//
//    public void setUri(String uri) {
//        this.uri = uri;
//    }
//
//    public String getIp() {
//        return ip;
//    }
//
//    public void setIp(String ip) {
//        this.ip = ip;
//    }
//
//    public LocalDateTime getHitTime() {
//        return hitTime;
//    }
//
//    public void setHitTime(LocalDateTime hitTime) {
//        this.hitTime = hitTime;
//    }
//
//    @Override
//    public String toString() {
//        return "EndpointHitEntity{" +
//                "id=" + id +
//                ", app='" + app + '\'' +
//                ", uri='" + uri + '\'' +
//                ", ip='" + ip + '\'' +
//                ", hitTime=" + hitTime +
//                '}';
//    }
//
//}
