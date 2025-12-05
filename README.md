# meta-yocto-iceoryx2

Yocto Layer for Eclipse iceoryx2™

## Manual setup

### Prerequisites

This setup is based on Ubuntu 24.04.

If you are running in a docker environment with a minimal Ubuntu setup,
ensure that a timezone is set before running the other commands. One option
to succeed with this is:

```sh
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y tzdata
```

Before we start with the Yocto build, ensure that the following dependencies 
are installed:

```sh
sudo apt install -y \
    chrpath \
    cpio \
    diffstat \
    file \
    gawk \
    gcc \
    git \
    language-pack-en \
    lz4 \
    pip \
    python3 \
    wget \
    zstd
```

### Setup the Yocto build

Move to your project directory and checkout the Poky distribution and switch to the `scarthgap` branch:

```sh
git clone git://git.yoctoproject.org/poky --branch scarthgap
cd poky
```

Now source the `oe-init-build-env` and build the base system.

```sh
source oe-init-build-env
```

This will also change the location to the build directory. If you don't want
to have the build directory inside the `poky` source checkout, you can pass a
path to `oe-init-build-env`.

Since Yocto can consume quite a lot of disk space, we add
`INHERIT += "rm_work"` to `conf/local.conf`.

```sh
echo 'INHERIT += "rm_work"' >> conf/local.conf
```

Now it's time to build the base image. This can take quite some time.

```sh
bitbake core-image-base
```

### Build iceoryx2-image-full

In order to build iceoryx2, we need to add more dependencies. Navigate to the
`poky` directory, clone `meta-openenbedded`, `meta-rust-bin` and `meta-iceoryx2` and add the layers to `conf/bblayers.conf`:

```sh
git clone https://github.com/openembedded/meta-openembedded.git --branch scarthgap
bitbake-layers add-layer meta-openembedded/meta-oe

git clone https://github.com/rust-embedded/meta-rust-bin.git
bitbake-layers add-layer meta-rust-bin

git clone https://github.com/eclipse-iceoryx/meta-iceoryx2.git
bitbake-layers add-layer meta-iceoryx2
```

The `meta-oe` layer is needed in order to build the tests with gTest and
the `meta-iceoryx2-bin` layer is required to build iceoryx2 itself.

We are now ready to build the `iceoryx2-image-full`, which does not only
contain the libraries and CLI tools but also the examples and tests.

For enable unit tests of iceoryx2 and iceoryx2-cxx, add to local.conf

```sh
DISTRO_FEATURES:append = " ptest"
```

```sh
bitbake iceoryx2-image-full
```

By default, the `qemux86-64` image is build. To select a different image, just
uncomment the desired platform from the `Machine Selection`.

Once the build process completes, the image can be used with the
`runqemu qemux86-64` command.

## Troubleshooting

In Yocto one of the main requirements is that package versions increases
in a linear way to ensure compatibility with package managers like `rpm`.
Since we introduced package versions in `meta-iceoryx2` layer config
it may happen that version errors are produced while building:

```sh
ERROR: iceoryx2-cxx-0.7.0-r0 do_packagedata: QA Issue: Package version for package iceoryx2-cxx-src went backwards which would break package feeds (from 0:git-r0 to 0:0.7.0-r0) [version-going-backwards]
```

It is recommended to cleanup the build cache of the iceoryx2 packages before building the image:

```sh
bitbake -c cleanall iceoryx-hoofs-subset \
                    iceoryx-platform-minimal \
                    iceoryx2 \
                    iceoryx2-validation-suite \
                    iceoryx2-c \
                    iceoryx2-c-examples \
                    iceoryx2-cmake-modules \
                    iceoryx2-cxx \
                    iceoryx2-cxx-examples
```

### Run an iceoryx2 example

The examples are located under `/usr/bin/iceoryx2/examples`, with `rust`, `c` and `cxx` subfolders.

When qemu is used to run the image, the user needs to login with `root` and no
password. Once logged in, the following command can be used to run the
publish-subscribe examples.

```sh
/usr/bin/iceoryx2/examples/rust/publish_subscribe_subscriber &
/usr/bin/iceoryx2/examples/rust/publish_subscribe_publisher
```

On the terminal, the output of the applications should now appear.

When the publisher is stopped, the iceoryx2 CLI can be used to list the
information from the still running subscriber example.

```sh
iox2 node list
iox2 service list
```

These command will list the available information about the iceoryx2 application.
For more details about the iceoryx2 CLI, please visit https://github.com/eclipse-iceoryx/iceoryx2/tree/main/iceoryx2-cli.

## How to build iceoryx2-image-full with kas

```sh
mkdir yocto-iceoryx2
cd yocto-iceoryx2
python3 -m venv .venv
source .venv/bin/activate
pip3 install kas
git clone https://github.com/eclipse-iceoryx/meta-iceoryx2.git
```

- build with yocto kirkstone - machine=qemux86-64

```sh
kas build meta-iceoryx2/kas/kirkstone.yml
```

- build with yocto scarthgap - machine=qemux86-64

```sh
kas build meta-iceoryx2/kas/scarthgap.yml
```

### Run ptest with kas MACHINE qemux86-64

- Bootup QEMU

```sh
kas shell meta-iceoryx2/kas/scarthgap.yml -c 'runqemu kvm nographic'
```

- Login and use ptest-runner running test cases

```sh
# For running iceoryx2 unit tests
ptest-runner iceoryx2
# For running iceoryx2-cxx unit tests
ptest-runner iceoryx2-cxx
```
