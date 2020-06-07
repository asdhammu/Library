# Library Management Project

Prerequisites:

Create schema with name of 'librarymanagement' in MySql. Run the following command in mysql command line

 - create schema librarymanagement
 - git clone https://github.com/asdhammu/Library.git
 
# For Services.: 

 - update password value of 'spring.datasource.password' in the application-dev.properties if different from 'admin'

 - cd services 

 - mvn spring-boot: run ( On first run, load will be inserted to database. This may be take from 15 - 30 minutes)


# For Front end: 

 - cd FrontEnd

 - npm install

 - npm run start
