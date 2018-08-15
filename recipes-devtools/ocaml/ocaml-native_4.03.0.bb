DESCRIPTION = "Objective Caml Compiler"
SECTION = "devel"
LICENSE = "QPL"

SRC_URI = " \
    https://github.com/ocaml/ocaml/archive/${PV}.tar.gz \
    file://ocaml-redirect \
    "

LIC_FILES_CHKSUM = "file://LICENSE;md5=1d53f1a1639ae7a362cf05c3a6c466c2"

SRC_URI[md5sum] = "4ddf4977de7708f11adad692c63e87ec"
SRC_URI[sha256sum] = "fef80a338099bffc56e4d1ef35146336195a4a9e2086e8cd186c402805503a0a"


inherit native

# we will install OCaml binaries and libraries in work-shared. The sysroot
# will contain symlinks only. This is necessary because OCaml does not handle
# relative paths very well: for example the compiler has a built-in hard-coded
# path to its libraries ('ocamlc -where')

SHARED_D = "${TMPDIR}/work-shared/ocaml/ocaml-${PV}-${PR}/${BUILD_SYS}"

do_configure () {
    cd ${S}
    ./configure  \
                -bindir ${SHARED_D}/usr/bin \
                -libdir ${SHARED_D}/usr/lib \
                -mandir ${SHARED_D}/usr/share/man \
                -host ${BUILD_SYS} \
                -verbose \
                -no-graph
}

do_compile() {
	oe_runmake world
    oe_runmake bootstrap
    oe_runmake opt
    oe_runmake opt.opt
}

do_install() {
    # this will install OCaml in the work-shared directory
    oe_runmake install

    # create regular sysroot directory which contains links to actual OCaml
    # binaries in work-shared
    rm -rf ${D}${bindir}
    install -d  ${D}${bindir}

    # ideally we would want direct symlinks to the work-shared binaries, however
    # Yocto does not allow absolute paths. So need to patch the path into a
    # shell script and symlink to the shell script instead.
    sed -i "s|^WORK_SHARED_PATH.*|WORK_SHARED_PATH=${SHARED_D}/usr/bin|" ${WORKDIR}/ocaml-redirect
    install -m 755 ${WORKDIR}/ocaml-redirect ${D}${bindir}
    cd ${D}${bindir}
    for i in `ls ${SHARED_D}/usr/bin`; do
        # ocamlrun and ocamlyacc are the exception, they are relocatable
        if [ "$i" = "ocamlrun" ] || [ "$i" = "ocamlyacc" ]; then
            cp ${SHARED_D}/usr/bin/$i .
        else
            ln -s ocaml-redirect $i
        fi
    done
}
