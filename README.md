# GXServerClient - Java client for GeneXus Server services


## About
A lightweight implementation of clients to Genexus Server services (eg: get hosted kbs, get revisions, etc.).

Group Id: com.genexus.gxserver
ArtifactId: gxserver-client

## Usage examples

1. Create a Client, eg: TeamWorkService2Client

```java
  TeamWorkService2Client twClient = new TeamWorkService2Client(
          "https://samples.genexusserver.com/v17",
          "GeneXus Account\User",
          getPassword()
  );
```

2. Get KBs and their basic info

```java
  KBList kbs = twClient.getHostedKBs();
  for (KBInfo kb : kbs) {
    String kbName = kb.name;
    String kbDescription = kb.description;
    URL kbUrl = kb.url;
    String kbBase64Image = kb.base64Image;
    String kbTags = kb.tags;
    KBInfo.TeamDevMode kbTeamDevMode = kb.teamDevMode;
    String kbPublishUser = kb.publishUser;
    LocalDate kbPublishDate = kb.publishDate;

    // ...
  }
```

3. Get KB versions and their info

```java
  VersionList versions = twClient.getVersions(kb.name);
  for (VersionInfo version : versions) {
    int versionId = version.id;
    String versionName = version.name;
    UUID versionGuid = version.guid;
    UUID versionType = version.type;
    Boolean versionIsTrunk = version.isTrunk;
    int versionParentId = version.parentId;
    Boolean versionIsFrozen = version.isFrozen;
    int versionRemindsCount = version.remindsCount;

    // ...
  }
 ```

4. Construct a Revisions Query and iterate results

  ```java
  // get yesterday's Date
  Calendar cal = Calendar.getInstance();
  cal.add(Calendar.DATE, -1);
  Date yesterday = cal.getTime();

  // construct query
  RevisionsQuery query = new RevisionsQuery(twClient, kb.name, version.name, yesterday);

  // iterate revisions
  for (RevisionInfo revision : query) {
    int revisionId = revision.id;
    UUID revisionGuid = revision.guid;
    String revisionAuthor = revision.author;
    Date revisionDate = revision.date;
    boolean revisionIsDisabled = revision.isDisabled;
    String revisionComment = revision.comment;

    // iterate revision actions
    for (ActionInfo action : revision.getActions()) {
      UUID actionObjectGuid = action.objectGuid;
      String actionObjectKey = action.objectKey;
      String actionObjectType = action.objectType;
      String actionObjectName = action.objectName;
      String actionObjectDescription = action.objectDescription;
      ActionInfo.ActionType actionActionType = action.actionType;
      String actionUserName = action.userName;
      Date actionEditedTimestamp = action.editedTimestamp;

      // ...
    }

    // ...                
  }
```

   **Note:** See [RevisionsQuery](src/main/java/com/genexus/gxserver/client/clients/RevisionsQuery.java) for other construction options.

5. Get paginated version revisions and their actions

```java
  int pageNumber = 1;
  String filterQuery = "";
  RevisionList revisions = twClient.getRevisions(kb.name, version.id, filterQuery, pageNumber);
  for (RevisionInfo revision : revisions) {
    int revisionId = revision.id;
    UUID revisionGuid = revision.guid;
    String revisionAuthor = revision.author;
    Date revisionDate = revision.date;
    boolean revisionIsDisabled = revision.isDisabled;
    String revisionComment = revision.comment;

    for (ActionInfo action : revision.getActions()) {
      UUID actionObjectGuid = action.objectGuid;
      String actionObjectKey = action.objectKey;
      String actionObjectType = action.objectType;
      String actionObjectName = action.objectName;
      String actionObjectDescription = action.objectDescription;
      ActionInfo.ActionType actionActionType = action.actionType;
      String actionUserName = action.userName;
      Date actionEditedTimestamp = action.editedTimestamp;

      // ...
    }

    // ...
  }
```

