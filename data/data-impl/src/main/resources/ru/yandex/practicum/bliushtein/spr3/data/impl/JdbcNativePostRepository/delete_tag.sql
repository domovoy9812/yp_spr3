delete from tags where post = ? and name in (SELECT UNNEST(?))