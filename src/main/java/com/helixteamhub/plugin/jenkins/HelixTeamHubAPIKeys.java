package com.helixteamhub.plugin.jenkins;

public class HelixTeamHubAPIKeys {

    private static final String FORMAT = "company_key=\"%s\",account_key=\"%s\"";

    private String companyKey;
    private String accountKey;

    public HelixTeamHubAPIKeys(String companyKey, String accountKey) {
        this.companyKey = companyKey;
        this.accountKey = accountKey;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String toString() {
        return String.format(FORMAT, companyKey, accountKey);
    }

}
