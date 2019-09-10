/*
  specify which libraries to load:
    In the Governance Tier configuration file,
    these should be configurations common across
    all apps governed by this config.
*/
application_environments{
  dev{
    short_name = "dev"
    long_name = "Development"
    hosts = "dev.siglus.us"
  }

  qa{
    short_name = "qa"
    long_name = "QA"
    hosts = "qa.siglus.us"
  }

  uat{
    short_name = "uat"
    long_name = "UAT"
    hosts = "uat.siglus.us"
  }
  
  prod{
    short_name = "prod"
    long_name = "Production"
  }
}

libraries{
  merge = true
  slack
  gate
  // common
}