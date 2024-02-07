namespace java de

    typedef i32 int
    typedef Sensordata sensordata
    typedef list<Sensordata>  sensorDataList

    service DatabaseServiceRead {
            sensorDataList read();
    }

    service CheckAvailable {
             bool isAvailable();
        }

    service DatabaseServiceUpdate {
            void update(1:int id,2:int sensorid, 3:double temperature, 4:double humidity, 5:string date);
    }

    service DatabaseServiceCreate {
            void create(1:int sensorid, 2:double temperature, 3:double humidity, 4:string date);
    }

     service DatabaseServiceDelete {
            void remove(1:int id);
     }


    struct Sensordata{
        1:int sensorid;
        2:double temperature;
        3:double humidity;
        4:string date;
    }