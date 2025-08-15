# meta-yocto-iceoryx2
Yocto Layer for Eclipse iceoryx2™

### How to build iceoryx2-core-image-full with kas

```
$ mkdir yp-iceoryx
$ cd yp-iceoryx
$ python3 -m venv .venv
$ source .venv/bin/activate
$ pip3 install kas
$ git clone https://github.com/eclipse-iceoryx/meta-iceoryx2.git
```

- build with yocto kirkstone - machine=qemux86-64

```
$ kas build meta-iceoryx2/kas/kirkstone.yml
```

- build with yocto scarthgap - machine=qemux86-64

```
$ kas build meta-iceoryx2/kas/scarthgap.yml
```
