package com.project.schoolmagazine.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "flyway_schema_history", schema = "public", catalog = "schoolMagazine")
public class FlywaySchemaHistoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "installed_rank")
    private Integer installedRank;
    @Basic
    @Column(name = "version")
    private String version;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "script")
    private String script;
    @Basic
    @Column(name = "checksum")
    private Integer checksum;
    @Basic
    @Column(name = "installed_by")
    private String installedBy;
    @Basic
    @Column(name = "installed_on")
    private Timestamp installedOn;
    @Basic
    @Column(name = "execution_time")
    private Integer executionTime;
    @Basic
    @Column(name = "success")
    private Boolean success;

    public Integer getInstalledRank() {
        return installedRank;
    }

    public void setInstalledRank(Integer installedRank) {
        this.installedRank = installedRank;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    public String getInstalledBy() {
        return installedBy;
    }

    public void setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
    }

    public Timestamp getInstalledOn() {
        return installedOn;
    }

    public void setInstalledOn(Timestamp installedOn) {
        this.installedOn = installedOn;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlywaySchemaHistoryEntity that = (FlywaySchemaHistoryEntity) o;
        return Objects.equals(installedRank, that.installedRank) && Objects.equals(version, that.version) && Objects.equals(description, that.description) && Objects.equals(type, that.type) && Objects.equals(script, that.script) && Objects.equals(checksum, that.checksum) && Objects.equals(installedBy, that.installedBy) && Objects.equals(installedOn, that.installedOn) && Objects.equals(executionTime, that.executionTime) && Objects.equals(success, that.success);
    }

    @Override
    public int hashCode() {
        return Objects.hash(installedRank, version, description, type, script, checksum, installedBy, installedOn, executionTime, success);
    }
}
