DESCRIPTION = "Objective Caml Compiler"
SECTION = "devel"
LICENSE = "QPL"

inherit cross

SRC_URI = " \
    https://github.com/ocaml/ocaml/archive/${PV}.tar.gz \
    file://0001-add-sysroot-configure-option.patch \
    file://0010-add-arm32-cross-target.patch \
    file://ocaml-pathfixer \
    "

LIC_FILES_CHKSUM = "file://LICENSE;md5=1d53f1a1639ae7a362cf05c3a6c466c2"

SRC_URI[md5sum] = "4ddf4977de7708f11adad692c63e87ec"
SRC_URI[sha256sum] = "fef80a338099bffc56e4d1ef35146336195a4a9e2086e8cd186c402805503a0a"

DEPENDS += " \
    coreutils-native \
    ocaml-native \
    virtual/${TARGET_PREFIX}gcc \
    virtual/${TARGET_PREFIX}compilerlibs \
    "

PACKAGES = "${PN} ${PN}-runtime"

do_configure () {
    cd ${S}
    ./configure -prefix ${D} \
                -bindir "\$(PREFIX)${bindir}" \
                -target-bindir "/usr/bin" \
                -libdir "\$(PREFIX)${libdir}" \
                -mandir "\$(PREFIX)${mandir}" \
                -sysroot ${STAGING_DIR_TARGET} \
                -host ${BUILD_SYS} \
                -target ${TARGET_SYS} \
                -no-graph 
}

# we can't build the optional 'bootstrap' and 'opt.opt' targets when cross-compiling, 
# these require executing (cross-)compiled code (ocamlrun) on the host ma chine
do_compile() {
	oe_runmake world
    oe_runmake opt
}

#INSANE_SKIP_${PN} += " arch file-rdeps ldflags staticdev textrel"
#INSANE_SKIP_${PN}-dev += " dev-elf ldflags textrel"

OCAML_BYTECODE_APPS = " \
    ocaml \
    ocamlc \ 
    ocamlcp \ 
    ocamldebug \
    ocamldep \ 
    ocamldoc \ 
    ocamllex \ 
    ocamlmklib \ 
    ocamlmktop \ 
    ocamlobjinfo \  
    ocamlopt \ 
    ocamloptp \ 
    ocamlprof \
    "

do_install() {
    # run regular install
    oe_runmake install

    # OCaml bytecode applications contain a hard-coded 'ocamlrun' path which does
    # not work well with OE, where they will be copied to individual directories
    # for each OCaml application. The 'pathfixer' script will take care of this
    install -m 0755 ${WORKDIR}/ocaml-pathfixer ${D}${bindir}
    cd ${D}${bindir}
    for i in ${OCAML_BYTECODE_APPS}; do
        mv ${i} _${i}
        ln -s ocaml-pathfixer ${i}
    done

    # remove some crud
    rm -rf ${D}/usr/share
    rm -rf ${D}/usr/src
    rm -rf ${D}/usr/lib/.debug
    rm -rf ${D}/usr/bin/.debug
    rm -rf ${D}/usr/lib/stublibs/.debug
    
    # there are two 'ocamlrun' executables:
    #  a) a cross-compiled version in the 'byterun' directory which 
    #     belongs on the target system if so desired. 
    #  b) a native version that is required for building this package
    #     since the OCaml compiler bootstraps itself with the help 
    #     of 'ocamlrun'. The OCaml makefile puts this native version
    #     into the the /usr/bin directory when executing 'make install'
    install -d ${D}/usr/bin
    install -m 0755 ${B}/byterun/ocamlrun ${D}/usr/bin/ocamlrun-target
}

INHIBIT_SYSROOT_STRIP = "1"
#INHIBIT_PACKAGE_STRIP

#FILES_${PN} += " /usr/lib"
FILES_${PN}-runtime += " ${base_bindir}"


# OCaml installs a bunch of header and source files in the /usr/lib
# directory which the OE build system interprets as debug/src files.
# We suppress package splitting here, assuming that OCaml knows
# what it's doing. This may have to be revised.
#INHIBIT_PACKAGE_DEBUG_SPLIT = "1"