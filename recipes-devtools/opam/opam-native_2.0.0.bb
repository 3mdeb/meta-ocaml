DESCRIPTION = "Package Manager for OCaml"
SECTION = "devel"
LICENSE = "GPLv2"

SRC_URI = " \
    https://github.com/ocaml/opam/archive/${PV}.tar.gz \
    file://0001-jbuilder-pathfix.patch \
    "

LIC_FILES_CHKSUM = "file://LICENSE;md5=d9e77cf0b09010013d038358f983b42a"

SRC_URI[md5sum] = "12b9835e43ff023a0b04a922f0ed9db5"
SRC_URI[sha256sum] = "1e9b47a66a952571b32217c58d0c58d2d66706083969dcfa1e42a60ecce30121"
# opam has an autotools-style 'configure' file, but the makefile has been
# created manually.
inherit autotools-brokensep native

DEPENDS += " \
    ocaml-native \
    bzip2-native \
    rsync-native \
    unzip-native \
    coreutils-native \
    gcc \
    "

# somehow this seems to be required...
PARALLEL_MAKE = "-j 1"

SHARED_D = "${TMPDIR}/work-shared/ocaml/opam-${PV}-${PR}"

# not all of these paths seem to be observed as usual. TBD.
EXTRA_OECONF = "\
    --prefix=${SHARED_D}/usr/ \
    --bindir=${SHARED_D}/usr/bin \
    --libdir=${SHARED_D}/usr/lib \
    "

do_compile_prepend () {
    oe_runmake lib-ext
}

# final install directory comprises of 'DESTDIR' and 'prefix'. Explicitly clear
# 'DESTDIR' so that the opam makefile does not set it.
do_install () {
    # install in work-shared directory
    oe_runmake install DESTDIR=""

    # initialize, create opam root in work-shared
    rm -rf ${SHARED_D}/../opam-root-${BUILD_SYS}
    ${SHARED_D}/usr/bin/opam init \
        --disable-sandboxing \
        --root=${SHARED_D}/../opam-root-${BUILD_SYS} \
        --no-setup

    # copy binaries to regular destination directory so that they get
    # included in sysroot for other packages that depend on this one
    install -d ${D}${bindir}
    cp ${SHARED_D}/usr/bin/* ${D}${bindir}
}
