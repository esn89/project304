
CREATE TABLE Item (
        item_upc INTEGER not null,
        item_title VARCHAR(25),
        item_type VARCHAR(3),
        item_category VARCHAR(12), 
        item_company VARCHAR(25),
        item_year INTEGER,
        item_price FLOAT,
        item_stock NUMBER,
primary key (item_upc));


CREATE TABLE LeadSinger (
        item_upc INTEGER not null,
        singer_name VARCHAR(25) not null,
        primary key (item_upc, singer_name),
        foreign key (item_upc) references Item);


CREATE TABLE HasSong (
        item_upc INTEGER not null,
        song_title VARCHAR(50) not null,
        primary key (item_upc, song_title), 
         foreign key (item_upc) references Item);

CREATE TABLE Purchase (
        purchase_receiptid INTEGER, 
        purchase_date DATE,
        customer_cid INTEGER not null,
        custorder_card# INTEGER,
        custorder_expiryDate DATE, 
        purchase_expectedDate DATE,
        purchase_deliveredDate DATE,
        primary key (purchase_receiptid),
        foreign key (customer_cid) references Customer);



CREATE TABLE PurchaseItem (
        purchase_receiptId INTEGER not null,
        item_upc INTEGER not null,
        quantity INTEGER,
        primary key (purchase_receiptId, item_upc),
        foreign key (purchase_receiptId) references Purchase,
                 foreign key (item_upc) references item );


CREATE TABLE Customer (
        customer_cid INTEGER not null,
        customer_password varchar(30),
        customer_name varchar(30),
        customer_address varchar(40),
        customer_phone INTEGER,
        primary key (customer_cid));


CREATE TABLE Return (
        return_retid INTEGER not null,
        return_date DATE,
        purchase_receiptId INTEGER,
        primary key (return_retid),
        foreign key (purchase_receiptId) references Purchase);


CREATE TABLE ReturnItem (
        return_retid INTEGER,
        item_upc INTEGER,
        returnItem_quantity INTEGER,
        primary key (return_retid, item_upc),
foreign key (return_retid) references Return,
foreign key (item_upc) references Item);