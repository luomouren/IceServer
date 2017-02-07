::2、部署分布式服务
G:
cd G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice
icegridadmin --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\registry.cfg -e "application add  'G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\grid.xml' "
::2.1、重新部署分布式服务
icegridadmin --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\registry.cfg -e "application update  'G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\grid.xml' "