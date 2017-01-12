cd G:\01Install\ZeroC\Ice-3.6.1\bin

::Ice Registry启动命令
icegridregistry --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\registry.cfg

::Ice Node启动命令
icegridnode --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\node1.cfg
icegridnode --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\node2.cfg

::Ice gridadmin启动命令
icegridadmin -u test -p test --Ice.Config=G:\00workspace\00workspace\01JCXT\IceServer\src\main\resources\ice\node1.cfg
