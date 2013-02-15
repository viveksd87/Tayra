package com.ee.tayra.io.criteria;

public enum OperationStrategy {
  DDL {
    @Override
    public boolean match(final String document,
            final String documentNamespace, final String incomingNs) {
      String collectionName = extractCollectionName(incomingNs);
      if (matchDbName(document, documentNamespace, incomingNs)) {
        if (collectionName == BLANK) {
            return true;
          }
          return matchCollectionInPayload(document, collectionName);
        }
        return false;
}
      private boolean matchCollectionInPayload(final String document,
        final String incomingCollectionName) {
        int startIndex = document.indexOf("\"o\" :") - 1;
        String payload = document.substring(startIndex).split(":", 2)[1];
        if (payload.contains("dropDatabase")) {
          return true;
        }
        if (payload.contains("create")) {
          return DDLStrategy.CREATECOLLECTION
                            .matchCollection(incomingCollectionName, payload);
        }
        if (payload.contains("drop")) {
          return DDLStrategy.DROPCOLLECTION
                            .matchCollection(incomingCollectionName, payload);
        }
        if (payload.contains("ns")) {
          return DDLStrategy.CREATEINDEX
                            .matchCollection(incomingCollectionName, payload);
        }
        if (payload.contains("deleteIndexes")) {
          return DDLStrategy.DROPINDEX
                            .matchCollection(incomingCollectionName, payload);
      }
        return false;
    }
},
  DML {
    @Override
    public boolean match(final String document, final String documentNamespace,
             final String incomingNs) {
      String collectionName = extractCollectionName(incomingNs);
       if (collectionName == BLANK) {
            return matchDbName(document, documentNamespace, incomingNs);
          }
          return matchDbAndCollectionName(document, documentNamespace,
            incomingNs);
}
    private boolean matchDbAndCollectionName(final String document,
        final String documentNamespace, final String incomingNs) {
        if (incomingNs.equals(documentNamespace)) {
        return true;
      }
      return false;
    }
  };
public abstract boolean match(final String document,
  final String documentNamespace, final String incomingNs);

public boolean matchDbName(final String document,
  final String documentNamespace, final String incomingNs) {
  String dbName = extractDbName(incomingNs);
  String documentDb = documentNamespace.split("\\.", 2)[0];
  if (dbName.equals(documentDb)) {
    return true;
  }
  return false;
}
private String extractDbName(final String incomingNS) {
    return incomingNS.split("\\.", 2)[0];
}

public String extractCollectionName(final String incomingNS) {
    try {
      return incomingNS.split("\\.", 2)[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      return BLANK;
  }
}
private static final String BLANK = "";
}
