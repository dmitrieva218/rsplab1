connect
'jdbc:derby://localhost:1527/Lab05DB1;create=true;user=root;password=toor';
-- создание схемы
CREATE SCHEMA Lab05DB1;
-- создание таблицы
create table file_info(id INT NOT NULL GENERATED ALWAYS AS IDENTITY,name varchar(20), created_at DATE ,
                       author varchar(100), path varchar(300));
CREATE UNIQUE INDEX item_id on file_info (id);

INSERT INTO file_info (name, created_at, author, path) VALUES ('Лаба 1','2020-12-30','ALEXCC','C:\Users\alexcc\YandexDisk\C#\Лабораторные\Лабораторная работа  №4.docx'),
    ('Лаба 2','2020-12-30','ALEXCC','C:\Users\alexcc\YandexDisk\C#\Лабораторные\Лабораторная работа  №2.docx'),
    ('Лаба 3','2020-12-30','ALEXCC','C:\Users\alexcc\YandexDisk\C#\Лабораторные\Лабораторная работа  №3.docx');
-- отключение и выход
disconnect;
exit;
