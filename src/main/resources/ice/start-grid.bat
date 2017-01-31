cd G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice
::Ice Registry启动命令
icegridregistry --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\registry.cfg
::部署分布式服务
icegridadmin --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\registry.cfg -e "application add  'G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\grid.xml' "
::重新部署分布式服务
icegridadmin --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\registry.cfg -e "application update  'G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\grid.xml' "
::Ice Node启动命令
icegridnode --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\node1.cfg
icegridnode --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\node2.cfg

::Ice gridadmin启动命令
icegridadmin -u test -p test --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\node1.cfg

::IceGrid ADMIN的主界面
cd G:\01Install\ZeroC\Ice-3.6.1\bin
java -jar icegridgui.jar